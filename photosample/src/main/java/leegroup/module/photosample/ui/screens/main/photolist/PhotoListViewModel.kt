package leegroup.module.photosample.ui.screens.main.photolist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import leegroup.module.core.extensions.orFalse
import leegroup.module.core.extensions.orZero
import leegroup.module.core.util.DispatchersProvider
import leegroup.module.designsystem.ui.models.ErrorState
import leegroup.module.designsystem.ui.viewmodel.StateViewModel
import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.GetPhotoListParam
import leegroup.module.photosample.domain.params.SaveFavoriteParam
import leegroup.module.photosample.domain.usecases.photofavorite.ObserveFavoriteListUseCase
import leegroup.module.photosample.domain.usecases.photofavorite.SaveFavoriteUseCase
import leegroup.module.photosample.domain.usecases.photolist.GetPhotoListFavoriteFilterUseCase
import leegroup.module.photosample.domain.usecases.photolist.GetPhotoListUseCase
import leegroup.module.photosample.domain.usecases.photolist.PhotoListQueryDelayUseCase
import leegroup.module.photosample.domain.usecases.photolist.SavePhotoListFavoriteFilterUseCase
import leegroup.module.photosample.ui.models.PhotoListUiModel
import leegroup.module.photosample.ui.models.PhotoUiModel
import leegroup.module.photosample.ui.models.mapToUiModels
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@Suppress("TooManyFunctions")
internal class PhotoListViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val photoListQueryDelayUseCase: PhotoListQueryDelayUseCase,
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val observeFavoriteListUseCase: ObserveFavoriteListUseCase,
    private val saveFavoriteUseCase: SaveFavoriteUseCase,
    private val getFavoriteFilterUseCase: GetPhotoListFavoriteFilterUseCase,
    private val saveFavoriteFilterUseCase: SavePhotoListFavoriteFilterUseCase
) : StateViewModel<PhotoListUiModel>(PhotoListUiModel()) {

    private var loadDataJob: Job? = null

    init {
        getFavoriteFilter()
        observeFavoriteList()
    }

    private fun observeFavoriteList() {
        observeFavoriteListUseCase.invoke()
            .onEach { favoriteList ->
                update { oldValue -> oldValue.updateFavorites(favoriteList) }
            }
            .flowOn(dispatchersProvider.io)
            .catch { Timber.e(it) }
            .launchIn(viewModelScope)
    }

    private fun getFavoriteFilter() {
        getFavoriteFilterUseCase.invoke().take(1)
            .onEach { isEnabled ->
                update { uiState -> uiState.copy(isFavoriteEnabled = isEnabled) }
            }
            .flowOn(dispatchersProvider.io)
            .catch { Timber.e(it) }
            .launchIn(viewModelScope)
    }

    fun handleFavoriteFilterClick() {
        update { uiState -> uiState.switchFavoriteFilter() }
        saveFavoriteFilterToLocal()
        reload()
    }

    fun handleFavoriteClick(item: PhotoUiModel) {
        viewModelScope.launch(dispatchersProvider.io) {
            val newItem = item.switchFavorite()
            saveFavoriteUseCase.invoke(
                SaveFavoriteParam(
                    id = newItem.id,
                    isFavorite = newItem.isFavorite
                )
            )
        }
    }

    fun handleQueryChanged(query: String) {
        if (query == getUiState().query) return

        update { uiState -> uiState.updateQuery(query) }
        reload()
    }

    private fun reload() {
        cancelCurrentLoading()
        update { uiState -> uiState.resetData() }
        loadMore()
    }

    fun handleActionLoadIfEmpty() {
        if (isUiStateEmpty()) {
            loadMore()
        }
    }

    fun loadMore() {
        if (canLoadMore().not()) return

        val currentState = getUiState()
        loadDataJob = viewModelScope.launch(dispatchersProvider.io) {
            showLoading()
            photoListQueryDelayUseCase.invoke(currentState.query)
            getPhotoListUseCase(currentState.createLoadMoreQueryParam())
                .injectLoading()
                .onEach { result -> onLoadMoreSuccess(result) }
                .flowOn(dispatchersProvider.io)
                .catch { e -> handleError(e) }
                .launchIn(viewModelScope)
                .join()
        }
    }

    private fun onLoadMoreSuccess(result: List<PhotoModelD>) {
        update { uiState ->
            uiState
                .plusPhotos(result.mapToUiModels())
                .copy(hasMore = result.isNotEmpty())
        }
    }

    private fun cancelCurrentLoading() {
        loadDataJob?.cancel()
    }

    private fun saveFavoriteFilterToLocal() {
        viewModelScope.launch(dispatchersProvider.io) {
            saveFavoriteFilterUseCase.invoke(getUiState().isFavoriteEnabled)
        }
    }

    private fun canLoadMore(): Boolean {
        return getUiState().canLoadMore() && isLoadingJobActive().not()
    }

    private fun isLoadingJobActive() = loadDataJob?.isActive.orFalse

    private fun isUiStateEmpty() = uiState.value.photos.isEmpty()

    override fun onErrorConfirmation(errorState: ErrorState) {
        super.onErrorConfirmation(errorState)
        when (errorState) {
            is ErrorState.Api, is ErrorState.Network -> loadMore()
            else -> Unit
        }
    }
}

private const val PER_PAGE = 30

private fun PhotoListUiModel.createLoadMoreQueryParam() = GetPhotoListParam(
    ids = if (isFavoriteEnabled) favoriteList.toList() else null,
    titleLike = query,
    since = photos.lastOrNull()?.id.orZero(),
    limit = PER_PAGE,
)

private fun PhotoListUiModel.canLoadMore(): Boolean {
    return hasMore && (isFavoriteEnabled && photos.size == favoriteList.size).not()
}

