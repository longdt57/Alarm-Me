package leegroup.module.photosample.domain.usecases

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.domain.repositories.PhotoListFavoriteFilterRepository
import leegroup.module.photosample.domain.usecases.photolist.GetPhotoListFavoriteFilterUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPhotoListFavoriteFilterUseCaseTest {

    private lateinit var getPhotoListFavoriteFilterUseCase: GetPhotoListFavoriteFilterUseCase
    private val photoListFavoriteFilterRepository: PhotoListFavoriteFilterRepository = mockk()

    @Before
    fun setUp() {
        // Initialize the use case with the mocked repository
        getPhotoListFavoriteFilterUseCase =
            GetPhotoListFavoriteFilterUseCase(photoListFavoriteFilterRepository)
    }

    @Test
    fun `test invoke returns the correct favorite filter value`() = runTest {
        // Given
        val expectedFavoriteFilter = true
        coEvery { photoListFavoriteFilterRepository.getFavoriteFilter() } returns flowOf(
            expectedFavoriteFilter
        )

        // When
        val result = getPhotoListFavoriteFilterUseCase.invoke().first()

        // Then
        assertEquals(expectedFavoriteFilter, result)
        verify { photoListFavoriteFilterRepository.getFavoriteFilter() }
    }
}
