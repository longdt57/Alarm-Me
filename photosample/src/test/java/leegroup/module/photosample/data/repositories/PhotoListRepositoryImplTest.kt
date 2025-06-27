package leegroup.module.photosample.data.repositories

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import leegroup.module.photosample.data.local.room.PhotoDao
import leegroup.module.photosample.data.local.room.entities.PhotoEntity
import leegroup.module.photosample.data.models.PhotoModel
import leegroup.module.photosample.data.remote.services.PhotoApiService
import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.GetPhotoListParam
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PhotoListRepositoryImplTest {

    private lateinit var repository: PhotoListRepositoryImpl
    private val appService: PhotoApiService = mockk()
    private val photoDao: PhotoDao = mockk()

    private val param = GetPhotoListParam(
        titleLike = "test",
        since = 0,
        limit = 10
    )

    @Before
    fun setup() {
        repository = PhotoListRepositoryImpl(appService, photoDao)
    }

    @Test
    fun `getRemote should return mapped data from ApiService`() = runTest {
        val apiResponse = listOf(
            PhotoModel(1, 1, "thumb1", "title1", "url1"),
            PhotoModel(2, 1, "thumb2", "title2", "url2")
        )
        coEvery {
            appService.getPhotos(
                titleLike = param.titleLike,
                since = param.since,
                limit = param.limit,
                ids = param.ids
            )
        } returns apiResponse

        val result = repository.getRemote(param)

        val expected = listOf(
            PhotoModelD(1, 1, "thumb1", "title1", "url1"),
            PhotoModelD(2, 1, "thumb2", "title2", "url2")
        )
        assertEquals(expected, result)
        coVerify {
            appService.getPhotos(
                titleLike = param.titleLike,
                since = param.since,
                limit = param.limit,
                ids = param.ids
            )
        }
    }

    @Test
    fun `getLocal should return mapped data from PhotoDao`() = runTest {
        val daoResponse = listOf(
            PhotoEntity(1, 1, "thumb1", "title1", "url1"),
            PhotoEntity(2, 1, "thumb2", "title2", "url2")
        )
        coEvery { photoDao.getPhotos(param.since, param.limit) } returns daoResponse

        val result = repository.getLocal(param)

        val expected = listOf(
            PhotoModelD(1, 1, "thumb1", "title1", "url1"),
            PhotoModelD(2, 1, "thumb2", "title2", "url2")
        )
        assertEquals(expected, result)
        coVerify { photoDao.getPhotos(param.since, param.limit) }
    }

    @Test
    fun `saveToLocal should upsert mapped data into PhotoDao`() = runTest {
        val domainData = listOf(
            PhotoModelD(1, 1, "thumb1", "title1", "url1"),
            PhotoModelD(2, 1, "thumb2", "title2", "url2")
        )
        val entityData = listOf(
            PhotoEntity(1, 1, "thumb1", "title1", "url1"),
            PhotoEntity(2, 1, "thumb2", "title2", "url2")
        )
        coEvery { photoDao.upsert(entityData) } just Runs

        repository.saveToLocal(domainData)

        coVerify { photoDao.upsert(entityData) }
    }
}
