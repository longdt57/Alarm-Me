package leegroup.module.photosample.domain.usecases

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.domain.params.SaveFavoriteParam
import leegroup.module.photosample.domain.repositories.PhotoFavoriteRepository
import leegroup.module.photosample.domain.usecases.photofavorite.SaveFavoriteUseCase
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveFavoriteUseCaseTest {

    private lateinit var useCase: SaveFavoriteUseCase
    private lateinit var photoFavoriteRepository: PhotoFavoriteRepository

    @Before
    fun setup() {
        photoFavoriteRepository = mockk()
        useCase = SaveFavoriteUseCase(photoFavoriteRepository)
    }

    @Test
    fun `invoke adds item to favorites when isFavorite is true`() = runTest {
        // Arrange
        val initialFavorites = setOf(1, 2, 3)
        val newFavoriteId = 4
        val expectedFavorites = setOf(1, 2, 3, 4)
        val param = SaveFavoriteParam(id = newFavoriteId, isFavorite = true)

        every { photoFavoriteRepository.getFavoriteList() } returns flowOf(initialFavorites)
        coEvery { photoFavoriteRepository.saveFavoriteList(any()) } just Runs

        // Act
        useCase.invoke(param)

        // Assert
        coVerify {
            photoFavoriteRepository.saveFavoriteList(expectedFavorites)
        }
    }

    @Test
    fun `invoke removes item from favorites when isFavorite is false`() = runTest {
        // Arrange
        val initialFavorites = setOf(1, 2, 3)
        val removedFavoriteId = 2
        val expectedFavorites = setOf(1, 3)
        val param = SaveFavoriteParam(id = removedFavoriteId, isFavorite = false)

        every { photoFavoriteRepository.getFavoriteList() } returns flowOf(initialFavorites)
        coEvery { photoFavoriteRepository.saveFavoriteList(any()) } just Runs

        // Act
        useCase.invoke(param)

        // Assert
        coVerify {
            photoFavoriteRepository.saveFavoriteList(expectedFavorites)
        }
    }

    @Test
    fun `invoke does nothing when removing an item not in the list`() = runTest {
        // Arrange
        val initialFavorites = setOf(1, 2, 3)
        val nonExistentId = 4
        val param = SaveFavoriteParam(id = nonExistentId, isFavorite = false)

        every { photoFavoriteRepository.getFavoriteList() } returns flowOf(initialFavorites)
        coEvery { photoFavoriteRepository.saveFavoriteList(any()) } just Runs

        // Act
        useCase.invoke(param)

        // Assert
        coVerify {
            photoFavoriteRepository.saveFavoriteList(initialFavorites)
        }
    }

    @Test
    fun `invoke does nothing when adding an already existing item`() = runTest {
        // Arrange
        val initialFavorites = setOf(1, 2, 3)
        val existingFavoriteId = 2
        val param = SaveFavoriteParam(id = existingFavoriteId, isFavorite = true)

        every { photoFavoriteRepository.getFavoriteList() } returns flowOf(initialFavorites)
        coEvery { photoFavoriteRepository.saveFavoriteList(any()) } just Runs

        // Act
        useCase.invoke(param)

        // Assert
        coVerify {
            photoFavoriteRepository.saveFavoriteList(initialFavorites)
        }
    }
}
