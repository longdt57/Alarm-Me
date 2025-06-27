package leegroup.module.photosample.ui.screens.photolist

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.designsystem.ui.models.ErrorState
import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.SaveFavoriteParam
import leegroup.module.photosample.domain.usecases.photofavorite.ObserveFavoriteListUseCase
import leegroup.module.photosample.domain.usecases.photofavorite.SaveFavoriteUseCase
import leegroup.module.photosample.domain.usecases.photolist.GetPhotoListFavoriteFilterUseCase
import leegroup.module.photosample.domain.usecases.photolist.GetPhotoListUseCase
import leegroup.module.photosample.domain.usecases.photolist.PhotoListQueryDelayUseCase
import leegroup.module.photosample.domain.usecases.photolist.SavePhotoListFavoriteFilterUseCase
import leegroup.module.photosample.ui.screens.PhotoMockUtil
import leegroup.module.photosample.ui.screens.main.photolist.PhotoListViewModel
import leegroup.module.test.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PhotoListViewModelTest : BaseUnitTest() {

    private lateinit var photoListViewModel: PhotoListViewModel
    private lateinit var photoListQueryDelayUseCase: PhotoListQueryDelayUseCase
    private lateinit var getPhotoListUseCase: GetPhotoListUseCase
    private lateinit var observeFavoriteListUseCase: ObserveFavoriteListUseCase
    private lateinit var saveFavoriteUseCase: SaveFavoriteUseCase
    private lateinit var getPhotoListFavoriteFilterUseCase: GetPhotoListFavoriteFilterUseCase
    private lateinit var saveFavoriteFilterUseCase: SavePhotoListFavoriteFilterUseCase

    @Before
    fun setUp() {
        // Mock dependencies
        photoListQueryDelayUseCase = mockk(relaxed = true)
        getPhotoListUseCase = mockk(relaxed = true)
        observeFavoriteListUseCase = mockk(relaxed = true)
        saveFavoriteUseCase = mockk(relaxed = true)
        getPhotoListFavoriteFilterUseCase = mockk(relaxed = true)
        saveFavoriteFilterUseCase = mockk(relaxed = true)

        every { getPhotoListUseCase.invoke(any()) } returns flowOf(listOf(PhotoMockUtil.photoModel))
        every { observeFavoriteListUseCase.invoke() } returns flowOf()
        coEvery { saveFavoriteUseCase.invoke(any()) } returns Unit
        coEvery { photoListQueryDelayUseCase.invoke(any()) } returns Unit

        initViewModel()
    }

    private fun initViewModel() {
        // Initialize ViewModel with mocked dependencies
        photoListViewModel = PhotoListViewModel(
            dispatchersProvider = testDispatcherProvider,
            photoListQueryDelayUseCase = photoListQueryDelayUseCase,
            getPhotoListUseCase = getPhotoListUseCase,
            observeFavoriteListUseCase = observeFavoriteListUseCase,
            saveFavoriteUseCase = saveFavoriteUseCase,
            getFavoriteFilterUseCase = getPhotoListFavoriteFilterUseCase,
            saveFavoriteFilterUseCase = saveFavoriteFilterUseCase
        )
    }

    @Test
    fun `test handle LoadIfEmpty action triggers loadMore when empty`() = runTest {
        photoListViewModel.handleActionLoadIfEmpty()

        // Verify that loadMore is called if the list is empty
        coVerify { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test handle LoadIfEmpty action, handle error`() = runTest {
        every { getPhotoListUseCase.invoke(any()) } returns flow { throw RuntimeException() }

        photoListViewModel.handleActionLoadIfEmpty()

        // Verify that loadMore is called if the list is empty
        photoListViewModel.error.test {
            expectMostRecentItem() shouldBe ErrorState.Common
        }
    }

    @Test
    fun `test handle FavoriteClick action updates favorite status and calls savePhotoUseCase`() =
        runTest {
            val updatedPhoto = PhotoMockUtil.photoUiModel.copy(isFavorite = true)
            every { getPhotoListUseCase.invoke(any()) } returns flowOf(listOf(PhotoMockUtil.photoModel))
            every { observeFavoriteListUseCase.invoke() } returns flowOf(setOf(updatedPhoto.id))
            initViewModel()

            photoListViewModel.handleActionLoadIfEmpty()
            photoListViewModel.handleFavoriteClick(PhotoMockUtil.photoUiModel)

            // Verify savePhotoUseCase is called with updated photo
            coVerify {
                saveFavoriteUseCase.invoke(
                    SaveFavoriteParam(
                        updatedPhoto.id,
                        updatedPhoto.isFavorite
                    )
                )
            }

            // Assert the favorite status is updated in the UI
            photoListViewModel.uiState.test {
                val state = expectMostRecentItem()
                assertTrue(state.photos.any { it.id == updatedPhoto.id && it.isFavorite })
            }
        }

    @Test
    fun `test get Favorite Filter`() =
        runTest {
            every { getPhotoListFavoriteFilterUseCase.invoke() } returns flowOf(true)
            initViewModel()

            // Assert the favorite status is updated in the UI
            photoListViewModel.uiState.test {
                val state = expectMostRecentItem()
                assertTrue(state.isFavoriteEnabled)
            }
        }

    @Test
    fun `test get Favorite Filter fail`() =
        runTest {
            every { getPhotoListFavoriteFilterUseCase.invoke() } returns flow { throw RuntimeException() }
            initViewModel()

            // Assert the favorite status is updated in the UI
            photoListViewModel.uiState.test {
                val state = expectMostRecentItem()
                assertFalse(state.isFavoriteEnabled)
            }
        }

    @Test
    fun `test observe FavoriteClick handle error`() =
        runTest {
            every { observeFavoriteListUseCase.invoke() } returns flow { throw RuntimeException() }
            initViewModel()

            photoListViewModel.handleActionLoadIfEmpty()

            // Assert the favorite status is updated in the UI
            photoListViewModel.uiState.test {
                val state = expectMostRecentItem()
                assertFalse(state.photos.any { it.isFavorite })
            }
        }

    @Test
    fun `test handle Query action updates query and triggers loadMore`() = runTest {
        val newQuery = "new query"
        photoListViewModel.handleQueryChanged(newQuery)

        // Verify the query is updated in the UI model
        photoListViewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(newQuery, state.query)
        }

        // Verify that loadMore is triggered after query update
        coVerify { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test handle LoadMore action triggers getPhotoListUseCase`() = runTest {
        photoListViewModel.loadMore()

        // Verify that loadMore is called when state is not loading and hasMore is true
        coVerify { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test loadMore does not trigger if already loading`() = runTest {
        every { getPhotoListUseCase.invoke(any()) } returns flow {
            delay(50)
        }

        // Try to load more
        photoListViewModel.handleActionLoadIfEmpty()
        delay(20)
        photoListViewModel.loadMore()

        // Verify that getPhotoListUseCase.invoke is not called while loading
        coVerify(exactly = 1) { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test handleSuccess updates UI state correctly`() = runTest {
        val result = listOf(
            PhotoModelD(
                id = 1,
                albumId = 1,
                thumbnailUrl = "",
                title = "",
                url = "",
            ),
            PhotoModelD(
                id = 2,
                albumId = 2,
                thumbnailUrl = "",
                title = "",
                url = "",
            )
        )

        every { getPhotoListUseCase.invoke(any()) } returns flow { emit(result) }
        // Call handleSuccess directly
        photoListViewModel.handleActionLoadIfEmpty()

        // Verify that UI state is updated with new photos
        photoListViewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(2, state.photos.size)  // Two new photos should be added
            assertTrue(state.photos.any { it.id == 1 })
            assertTrue(state.photos.any { it.id == 2 })
        }
    }

    @Test
    fun `test handle Error action triggers loadMore on API or Network error`() = runTest {
        val errorState = ErrorState.Api()

        // Simulate an error
        photoListViewModel.onErrorConfirmation(errorState)

        // Verify loadMore is triggered after an API or Network error
        coVerify { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test handle Error action not triggers loadMore on API or Network error`() = runTest {
        val errorState = ErrorState.Common

        // Simulate an error
        photoListViewModel.onErrorConfirmation(errorState)

        // Verify loadMore is triggered after an API or Network error
        coVerify(exactly = 0) { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test loadIfEmpty triggers loadMore when list is empty`() = runTest {
        // Set the state with an empty list
        photoListViewModel.handleActionLoadIfEmpty()

        // Verify that loadMore is called
        coVerify { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test loadIfEmpty does not trigger loadMore when list is not empty`() = runTest {
        // Set the state with some data
        every { getPhotoListUseCase.invoke(any()) } returns flowOf(listOf(PhotoMockUtil.photoModel))

        // Call loadIfEmpty
        photoListViewModel.handleActionLoadIfEmpty()
        photoListViewModel.handleActionLoadIfEmpty()

        // Verify loadMore is not called because the list is not empty
        coVerify(exactly = 1) { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test handleSuccess updates UI with correct photos and hasMore flag`() = runTest {
        val newPhotos = listOf(
            PhotoModelD(
                id = 3,
                albumId = 3,
                title = "New Photo",
                url = "http://example.com/3",
                thumbnailUrl = "http://example.com/3",
            ),
            PhotoModelD(
                id = 4,
                albumId = 4,
                title = "Another New Photo",
                url = "http://example.com/4",
                thumbnailUrl = "http://example.com/4",
            )
        )

        // Mock API response
        every { getPhotoListUseCase.invoke(any()) } returns flowOf(newPhotos)

        // Call loadMore action to trigger photo loading
        photoListViewModel.handleActionLoadIfEmpty()

        // Verify that the UI state is updated with the new photos and hasMore flag
        photoListViewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals(2, state.photos.size)  // Check two new photos are added
            assertTrue(state.photos.any { it.id == 3 })  // Check new photo exists
            assertTrue(state.photos.any { it.id == 4 })  // Check another new photo exists
            assertTrue(state.hasMore)  // Check hasMore flag is true
        }
    }

    @Test
    fun `test handleQuery action does not trigger loadMore if query is same as current query`() =
        runTest {
            val currentQuery = "current query"

            // Trigger Query action with the same query
            photoListViewModel.handleQueryChanged(currentQuery)
            photoListViewModel.handleQueryChanged(currentQuery)

            // Verify that loadMore is not triggered because query is not changed
            coVerify(exactly = 1) { getPhotoListUseCase.invoke(any()) }
        }

    @Test
    fun `test handleQuery action triggers loadMore if query is updated`() = runTest {
        photoListViewModel.handleActionLoadIfEmpty()

        val newQuery = "new query"
        photoListViewModel.handleQueryChanged(newQuery)

        // Verify that loadMore is triggered after query update
        coVerify(exactly = 2) { getPhotoListUseCase.invoke(any()) }
    }

    @Test
    fun `test onFavoriteFilterClick updates filter and calls saveFavoriteFilterToLocal`() =
        runTest {
            // Setup initial state
            val initialState = photoListViewModel.uiState.value

            // Trigger the FavoriteFilterClick action
            photoListViewModel.handleFavoriteFilterClick()

            // Verify that updateFavoriteFilter is called with the new filter status
            assertNotEquals(
                initialState.isFavoriteEnabled,
                photoListViewModel.uiState.value.isFavoriteEnabled
            )

            // Verify that saveFavoriteFilterToLocal is called with the new filter status
            coVerify { saveFavoriteFilterUseCase.invoke(any()) }

            // Verify that the filter status is updated in the UI
            photoListViewModel.uiState.test {
                val state = expectMostRecentItem()
                assertNotEquals(initialState.isFavoriteEnabled, state.isFavoriteEnabled)
            }
        }

    @Test
    fun `test saveFavoriteFilterToLocal is called on FavoriteFilterClick`() = runTest {
        // Trigger the FavoriteFilterClick action
        photoListViewModel.handleFavoriteFilterClick()

        // Verify that saveFavoriteFilterToLocal is called with the new filter status
        coVerify { saveFavoriteFilterUseCase.invoke(any()) }
    }

}
