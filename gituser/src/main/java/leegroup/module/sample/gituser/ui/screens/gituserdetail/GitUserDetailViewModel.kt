package leegroup.module.sample.gituser.ui.screens.gituserdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import leegroup.module.core.util.DispatchersProvider
import leegroup.module.core.util.SavedStateProvider
import leegroup.module.designsystem.ui.models.ErrorState
import leegroup.module.designsystem.ui.viewmodel.StateViewModel
import leegroup.module.sample.gituser.domain.models.GitUserDetailModel
import leegroup.module.sample.gituser.domain.usecases.gituser.GetGitUserDetailLocalUseCase
import leegroup.module.sample.gituser.domain.usecases.gituser.GetGitUserDetailRemoteUseCase
import leegroup.module.sample.gituser.ui.mapper.GitUserDetailUiMapper
import leegroup.module.sample.gituser.ui.models.GitUserDetailUiModel
import leegroup.module.sample.gituser.ui.screens.GitUserDestination
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class GitUserDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val savedStateProvider: SavedStateProvider<GitUserDestination.GitUserDetail.GitUserDetailNav>,
    private val dispatchersProvider: DispatchersProvider,
    private val getGitUserDetailLocalUseCase: GetGitUserDetailLocalUseCase,
    private val getGitUserDetailRemoteUseCase: GetGitUserDetailRemoteUseCase,
    private val gitUserDetailModelMapper: GitUserDetailUiMapper,
) : StateViewModel<GitUserDetailUiModel>(GitUserDetailUiModel()) {

    init {
        loadFromSavedStateHandle()
    }

    private fun loadFromSavedStateHandle() {
        val navModel = savedStateProvider.toRoute(savedStateHandle)
        setUserLogin(navModel.login)
    }

    private fun getLocal() {
        getGitUserDetailLocalUseCase(getLogin())
            .onEach { result ->
                handleSuccess(result)
            }
            .onEach {
                hideLoading() // Hide loading if local data is available
            }
            .flowOn(dispatchersProvider.io)
            .catch {
                Timber.e(it) // Not show local error to screen
            }
            .launchIn(viewModelScope)
    }

    private fun fetchRemote() {
        getGitUserDetailRemoteUseCase(getLogin())
            .let {
                // Show loading if data is empty
                when {
                    isDataEmpty() -> it.injectLoading()
                    else -> it
                }
            }
            .onEach { result ->
                handleSuccess(result)
            }
            .flowOn(dispatchersProvider.io)
            .catch { e ->
                if (isDataEmpty()) {
                    handleError(e)  // Show error if data is empty
                } else {
                    Timber.e(e)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleSuccess(result: GitUserDetailModel) {
        update { oldValue ->
            gitUserDetailModelMapper.mapToUiModel(oldValue, result)
        }
    }

    private fun setUserLogin(login: String) {
        update { oldValue -> oldValue.copy(login = login) }
        getLocal()
        fetchRemote()
    }

    private fun isDataEmpty(): Boolean {
        return getUiState().name.isBlank()
    }

    private fun getLogin() = getUiState().login

    override fun onErrorConfirmation(errorState: ErrorState) {
        super.onErrorConfirmation(errorState)
        when (errorState) {
            is ErrorState.Api, is ErrorState.Network -> fetchRemote()
            else -> Unit
        }
    }
}
