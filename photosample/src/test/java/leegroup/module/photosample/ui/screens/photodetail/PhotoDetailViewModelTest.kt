package leegroup.module.photosample.ui.screens.photodetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import leegroup.module.core.util.JsonUtil
import leegroup.module.core.util.SavedStateProvider
import leegroup.module.photosample.domain.params.SaveFavoriteParam
import leegroup.module.photosample.domain.usecases.photofavorite.SaveFavoriteUseCase
import leegroup.module.photosample.ui.screens.main.PhotoDetailNav
import leegroup.module.photosample.ui.screens.main.photodetail.PhotoDetailViewModel
import leegroup.module.test.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PhotoDetailViewModelTest : BaseUnitTest() {

    private lateinit var viewModel: PhotoDetailViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var savedStateProvider: SavedStateProvider<PhotoDetailNav>
    private lateinit var saveFavoriteUseCase: SaveFavoriteUseCase

    private val photoDetailNav = PhotoDetailNav(
        id = 1,
        albumId = 101,
        thumbnailUrl = "https://example.com/thumbnail.jpg",
        title = "Sample Photo",
        url = "https://example.com/photo.jpg",
        isFavorite = false
    )


    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle(JsonUtil.encodeToMap(photoDetailNav))
        savedStateProvider = mockk()
        saveFavoriteUseCase = mockk(relaxed = true)

        every { savedStateProvider.toRoute(any()) } returns photoDetailNav

        viewModel = PhotoDetailViewModel(
            savedStateHandle = savedStateHandle,
            savedStateProvider = savedStateProvider,
            dispatchersProvider = testDispatcherProvider,
            saveFavoriteUseCase = saveFavoriteUseCase
        )
    }

    @Test
    fun `loadFromSavedStateHandle should populate uiModel correctly`() = runTest {
        viewModel.uiState.test {
            val detailUiModel = expectMostRecentItem()
            assertEquals(photoDetailNav.id, detailUiModel.id)
            assertEquals(photoDetailNav.title, detailUiModel.title)
            assertEquals(photoDetailNav.url, detailUiModel.url)
            assertEquals(photoDetailNav.isFavorite, detailUiModel.isFavorite)
        }

    }

    @Test
    fun `handleAction with SwitchFavorite should toggle favorite state`() = runTest {
        // Arrange
        val initialUiModel = viewModel.uiState.value
        assertFalse(initialUiModel.isFavorite)

        // Act
        viewModel.switchFavorite()

        // Assert
        val updatedUiModel = viewModel.uiState.value
        assertTrue(updatedUiModel.isFavorite)
        coVerify { saveFavoriteUseCase.invoke(SaveFavoriteParam(1, true)) }
    }

    @Test
    fun `handleAction with SwitchFavorite toggles back to non-favorite state`() = runTest {
        // Arrange
        viewModel.switchFavorite()
        val updatedUiModel = viewModel.uiState.value
        assertTrue(updatedUiModel.isFavorite)

        // Act
        viewModel.switchFavorite()

        // Assert
        val toggledBackUiModel = viewModel.uiState.value
        assertFalse(toggledBackUiModel.isFavorite)
        coVerify { saveFavoriteUseCase.invoke(SaveFavoriteParam(1, false)) }
    }
}
