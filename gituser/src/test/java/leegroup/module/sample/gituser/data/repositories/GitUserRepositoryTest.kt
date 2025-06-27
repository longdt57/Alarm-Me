package leegroup.module.sample.gituser.data.repositories

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import leegroup.module.sample.gituser.data.MockUtil
import leegroup.module.sample.gituser.data.local.room.GitUserDao
import leegroup.module.sample.gituser.data.remote.services.GitUserApiService
import leegroup.module.sample.gituser.domain.repositories.GitUserRepository
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GitUserRepositoryTest {

    private lateinit var mockService: GitUserApiService
    private lateinit var mockUserDao: GitUserDao
    private lateinit var repository: GitUserRepository

    private val param = MockUtil.param
    private val sampleGitUsers = MockUtil.sampleGitUsers
    private val sampleGitUserModels = MockUtil.sampleGitUserModels


    @Before
    fun setUp() {
        mockService = mockk()
        mockUserDao = mockk()
        repository = GitUserRepositoryImpl(
            mockService,
            mockUserDao
        )
    }

    @Test
    fun `When request successful, it returns success`() = runTest {
        val expected = sampleGitUsers
        coEvery {
            mockService.getGitUser(
                since = param.since,
                perPage = param.perPage
            )
        } returns expected
        coEvery { mockUserDao.upsert(expected) } returns Unit

        repository.getRemote(param) shouldBe sampleGitUserModels
        coVerify(exactly = 1) { mockUserDao.upsert(expected) }
    }

    @Test
    fun `When request failed, it returns error`() = runTest {
        val expected = RuntimeException()
        coEvery {
            mockService.getGitUser(
                since = param.since,
                perPage = param.perPage
            )
        } throws expected

        try {
            repository.getRemote(param)
        } catch (ex: Exception) {
            ex shouldBe expected
        }
    }

    @Test
    fun `getLocal should return transformed local data`() = runTest {
        val expected = sampleGitUsers
        coEvery {
            mockUserDao.getUsers(
                since = param.since,
                perPage = param.perPage
            )
        } returns expected

        repository.getLocal(param) shouldBe sampleGitUserModels
    }

    @Test
    fun `getLocal should return empty when database is empty`() = runTest {
        coEvery {
            mockUserDao.getUsers(
                since = param.since,
                perPage = param.perPage
            )
        } returns emptyList()

        repository.getLocal(param) shouldBe emptyList()
    }
}
