package leegroup.module.photosample.domain.usecases

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.domain.usecases.photolist.PhotoListQueryDelayUseCase
import org.junit.Test

class PhotoListQueryDelayUseCaseTest {

    private val useCase = spyk(PhotoListQueryDelayUseCase())

    @Test
    fun `invoke with non-empty query should delay`() {
        // Mock the delay function
        mockkStatic("kotlinx.coroutines.DelayKt") // Static mocking for the `delay` function
        every { runBlocking { delay(PhotoListQueryDelayUseCase.QUERY_DELAY) } } just Runs

        // Execute the use case with a non-empty query
        runBlocking {
            useCase.invoke("non-empty")
        }

        // Verify that delay was called with the correct duration
        verify { runBlocking { delay(PhotoListQueryDelayUseCase.QUERY_DELAY) } }

        // Unmock the delay function to avoid side effects
        unmockkStatic("kotlinx.coroutines.DelayKt")
    }

    @Test
    fun `invoke with empty query should not delay`() = runTest {
        // Mock the delay function
        mockkStatic("kotlinx.coroutines.DelayKt") // Static mocking for the `delay` function
        coEvery { delay(any<Long>()) } just Runs

        // Execute the use case with an empty query
        runBlocking {
            useCase.invoke("")
        }

        // Verify that delay was never called
        coVerify(exactly = 0) { delay(any<Long>()) }

        // Unmock the delay function to avoid side effects
        unmockkStatic("kotlinx.coroutines.DelayKt")
    }
}
