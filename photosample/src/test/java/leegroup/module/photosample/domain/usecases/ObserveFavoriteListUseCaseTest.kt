package leegroup.module.photosample.domain.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.domain.repositories.PhotoFavoriteRepository
import leegroup.module.photosample.domain.usecases.photofavorite.ObserveFavoriteListUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveFavoriteListUseCaseTest {

    private lateinit var useCase: ObserveFavoriteListUseCase
    private lateinit var photoFavoriteRepository: PhotoFavoriteRepository

    @Before
    fun setup() {
        photoFavoriteRepository = mockk()
        useCase = ObserveFavoriteListUseCase(photoFavoriteRepository)
    }

    @Test
    fun `invoke should return favorite list from repository`() = runTest {
        // Arrange
        val expectedFavorites = setOf(1, 2, 3)
        every { photoFavoriteRepository.getFavoriteList() } returns flowOf(expectedFavorites)

        // Act
        val result = useCase.invoke().toList()

        // Assert
        assertEquals(listOf(expectedFavorites), result)
        verify { photoFavoriteRepository.getFavoriteList() }
    }
}
