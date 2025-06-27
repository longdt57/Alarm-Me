package leegroup.module.photosample.domain.usecases

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.GetPhotoListParam
import leegroup.module.photosample.domain.repositories.PhotoListRepository
import leegroup.module.photosample.domain.usecases.photolist.GetPhotoListUseCase
import org.junit.Before
import org.junit.Test

class GetPhotoListUseCaseTest {

    private lateinit var useCase: GetPhotoListUseCase
    private val repository: PhotoListRepository = mockk()

    private val localData = listOf(
        PhotoModelD(
            id = 1,
            albumId = 1,
            thumbnailUrl = "url1",
            title = "Title1",
            url = "url1",
        ),
        PhotoModelD(
            id = 2,
            albumId = 1,
            thumbnailUrl = "url2",
            title = "Title2",
            url = "url2",
        )
    )

    private val remoteData = listOf(
        PhotoModelD(
            id = 1,
            albumId = 1,
            thumbnailUrl = "url1",
            title = "Title1",
            url = "url1",
        ),
        PhotoModelD(
            id = 2,
            albumId = 1,
            thumbnailUrl = "url2",
            title = "Title2",
            url = "url2",
        )
    )

    @Before
    fun setup() {
        useCase = GetPhotoListUseCase(repository)
    }

    @Test
    fun `should emit local data if available`() = runTest {
        val param = GetPhotoListParam(titleLike = null, since = 0, limit = 10)
        coEvery { repository.getLocal(param) } returns localData

        useCase.invoke(param).test {
            val emittedItem = expectMostRecentItem()
            assert(emittedItem == localData)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { repository.getRemote(any()) }
        coVerify(exactly = 0) { repository.saveToLocal(any()) }
    }

    @Test
    fun `should fetch remote data and save to local if query is empty and no local data exists`() =
        runTest {
            val param = GetPhotoListParam(titleLike = null, since = 0, limit = 10)
            coEvery { repository.getLocal(param) } returns emptyList()
            coEvery { repository.getRemote(param) } returns remoteData
            coEvery { repository.saveToLocal(remoteData) } just Runs

            useCase.invoke(param).test {
                val emittedItems = expectMostRecentItem()
                assert(emittedItems == remoteData)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify { repository.getRemote(param) }
            coVerify { repository.saveToLocal(remoteData) }
        }

    @Test
    fun `should fetch remote data but not save to local if query is not empty`() = runTest {
        val param = GetPhotoListParam(titleLike = "query", since = 0, limit = 10)
        val remoteData = listOf(
            PhotoModelD(
                id = 3,
                albumId = 1,
                thumbnailUrl = "url3",
                title = "QueryTitle",
                url = "url3",
            )
        )
        coEvery { repository.getLocal(param) } returns emptyList()
        coEvery {
            repository.getRemote(param)
        } returns remoteData

        useCase.invoke(param).test {
            val emittedItems = expectMostRecentItem()
            assert(emittedItems == remoteData)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repository.getRemote(param) }
        coVerify(exactly = 0) { repository.saveToLocal(any()) }
    }

    @Test
    fun `should fetch remote data and save to local only if query is empty`() = runTest {
        val param = GetPhotoListParam(titleLike = "", since = 0, limit = 10)
        coEvery { repository.getLocal(param) } returns emptyList()
        coEvery {
            repository.getRemote(param)
        } returns remoteData
        coEvery { repository.saveToLocal(remoteData) } just Runs

        useCase.invoke(param).test {
            val emittedItems = expectMostRecentItem()
            assert(emittedItems == remoteData)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { repository.getRemote(param) }
        coVerify { repository.saveToLocal(remoteData) }
    }
}
