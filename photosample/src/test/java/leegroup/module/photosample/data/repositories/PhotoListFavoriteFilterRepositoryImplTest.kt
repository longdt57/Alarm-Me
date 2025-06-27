package leegroup.module.photosample.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.data.local.datastore.PhotoDataStore
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PhotoListFavoriteFilterRepositoryImplTest {

    private lateinit var photoListFavoriteFilterRepository: PhotoListFavoriteFilterRepositoryImpl
    private val appDataStore: PhotoDataStore = mockk()

    @Before
    fun setUp() {
        // Initialize the repository with the mocked data store
        photoListFavoriteFilterRepository = PhotoListFavoriteFilterRepositoryImpl(appDataStore)
    }

    @Test
    fun `test getFavoriteFilter returns correct value`() = runTest {
        // Given
        val expectedValue = true
        coEvery { appDataStore.getFavoriteFilter() } returns flowOf(expectedValue)

        // When
        val result = photoListFavoriteFilterRepository.getFavoriteFilter()

        // Then
        assertEquals(
            expectedValue,
            result.first()
        )  // Getting the first emitted value from the Flow
        verify { appDataStore.getFavoriteFilter() }
    }

    @Test
    fun `test setFavoriteFilter calls dataStore with correct value`() = runTest {
        // Given
        val valueToSet = false

        coEvery { appDataStore.setFavoriteFilter(false) } returns Unit
        // When
        photoListFavoriteFilterRepository.setFavoriteFilter(valueToSet)

        // Then
        coVerify { appDataStore.setFavoriteFilter(valueToSet) }
    }
}
