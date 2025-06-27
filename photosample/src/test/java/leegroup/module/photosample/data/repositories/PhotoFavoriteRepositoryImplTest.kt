package leegroup.module.photosample.data.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import leegroup.module.photosample.data.local.datastore.PhotoDataStore
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoFavoriteRepositoryImplTest {

    private lateinit var repository: PhotoFavoriteRepositoryImpl
    private val mockDataStore: PhotoDataStore = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = PhotoFavoriteRepositoryImpl(mockDataStore)
    }

    @Test
    fun `getFavoriteList should map String set to Int set`() = runTest {
        // Arrange
        val mockStringSet = setOf("1", "2", "3")
        every { mockDataStore.getFavoriteList() } returns flowOf(mockStringSet)

        // Act
        val result = repository.getFavoriteList().first()

        // Assert
        assertEquals(setOf(1, 2, 3), result)
        verify { mockDataStore.getFavoriteList() }
    }

    @Test
    fun `saveFavoriteList should map Int set to String set`() = runTest {
        // Arrange
        val inputIntSet = setOf(4, 5, 6)
        val slotStringSet = slot<Set<String>>()
        coEvery { mockDataStore.setFavoriteList(capture(slotStringSet)) } returns Unit

        // Act
        repository.saveFavoriteList(inputIntSet)

        // Assert
        assertEquals(setOf("4", "5", "6"), slotStringSet.captured)
        coVerify { mockDataStore.setFavoriteList(setOf("4", "5", "6")) }
    }
}
