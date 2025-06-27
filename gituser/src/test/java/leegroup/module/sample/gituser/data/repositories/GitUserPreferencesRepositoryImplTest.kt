package leegroup.module.sample.gituser.data.repositories

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.sample.gituser.data.local.datastore.GitUserDataStore
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GitUserPreferencesRepositoryImplTest {

    private lateinit var appPreferencesRepository: GitUserPreferencesRepositoryImpl
    private lateinit var mockAppDataStore: GitUserDataStore

    @Before
    fun setUp() {
        mockAppDataStore = mockk()
        appPreferencesRepository = GitUserPreferencesRepositoryImpl(mockAppDataStore)
    }

    @Test
    fun `getAppPreference returns true when value is null`() = runTest {
        // Arrange
        every { mockAppDataStore.getAppPreference() } returns flowOf(null)

        // Act & Assert
        appPreferencesRepository.getAppPreference().test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAppPreference returns stored value when not null`() = runTest {
        // Arrange
        every { mockAppDataStore.getAppPreference() } returns flowOf(false)

        // Act & Assert
        appPreferencesRepository.getAppPreference().test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateAppPreference calls AppDataStore setValue with correct values`() = runTest {
        // Arrange
        val newPreferenceValue = false
        coEvery {
            mockAppDataStore.setAppPreference(newPreferenceValue)
        } just Runs

        // Act
        appPreferencesRepository.updateAppPreference(newPreferenceValue)

        // Assert
        coVerify { mockAppDataStore.setAppPreference(newPreferenceValue) }
    }
}
