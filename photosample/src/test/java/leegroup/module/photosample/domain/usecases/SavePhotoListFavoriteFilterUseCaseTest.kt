package leegroup.module.photosample.domain.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.domain.repositories.PhotoListFavoriteFilterRepository
import leegroup.module.photosample.domain.usecases.photolist.SavePhotoListFavoriteFilterUseCase
import org.junit.Before
import org.junit.Test

class SavePhotoListFavoriteFilterUseCaseTest {

    private lateinit var savePhotoListFavoriteFilterUseCase: SavePhotoListFavoriteFilterUseCase
    private val photoListFavoriteFilterRepository: PhotoListFavoriteFilterRepository = mockk()

    @Before
    fun setUp() {
        // Initialize the use case with the mocked repository
        coEvery { photoListFavoriteFilterRepository.setFavoriteFilter(any()) } returns Unit
        savePhotoListFavoriteFilterUseCase =
            SavePhotoListFavoriteFilterUseCase(photoListFavoriteFilterRepository)
    }

    @Test
    fun `test invoke calls setFavoriteFilter with correct argument`() = runTest {
        // Given
        val isEnable = true

        // When
        savePhotoListFavoriteFilterUseCase.invoke(isEnable)

        // Then
        coVerify { photoListFavoriteFilterRepository.setFavoriteFilter(isEnable) }
    }
}
