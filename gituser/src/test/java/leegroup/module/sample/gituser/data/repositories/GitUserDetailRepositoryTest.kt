package leegroup.module.sample.gituser.data.repositories

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import leegroup.module.sample.gituser.data.MockUtil
import leegroup.module.sample.gituser.data.local.room.GitUserDetailDao
import leegroup.module.sample.gituser.data.remote.services.GitUserApiService
import leegroup.module.sample.gituser.domain.repositories.GitUserDetailRepository
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GitUserDetailRepositoryTest {

    private lateinit var mockService: GitUserApiService
    private lateinit var mockUserDao: GitUserDetailDao
    private lateinit var repository: GitUserDetailRepository

    private val login = MockUtil.GIT_USER_DETAIL_LOGIN
    private val gitUserDetail = MockUtil.gitUserDetail
    private val gitUserDetailModel = MockUtil.gitUserDetailModel

    @Before
    fun setUp() {
        mockService = mockk()
        mockUserDao = mockk()
        repository = GitUserDetailRepositoryImpl(mockService, mockUserDao)
    }

    @Test
    fun `When request successful, it returns success`() = runTest {
        val expected = gitUserDetail
        coEvery { mockService.getGitUserDetail(login) } returns expected
        coEvery { mockUserDao.upsert(expected) } returns expected.id

        repository.getRemote(login) shouldBe gitUserDetailModel
        coVerify(exactly = 1) { mockUserDao.upsert(expected) }
    }

    @Test
    fun `When request failed, it returns error`() = runTest {
        val expected = RuntimeException()
        coEvery { mockService.getGitUserDetail(login) } throws expected

        try {
            repository.getRemote(login)
        } catch (e: Exception) {
            e shouldBe expected
        }
    }

    @Test
    fun `getLocal should return transformed local data`() = runTest {
        val expected = gitUserDetail
        coEvery { mockUserDao.getUserDetail(login) } returns expected

        repository.getLocal(login) shouldBe gitUserDetailModel
    }

    @Test
    fun `getLocal should return nothing when database is empty`() = runTest {
        coEvery { mockUserDao.getUserDetail(login) } returns null

        repository.getLocal(login) shouldBe null
    }
}
