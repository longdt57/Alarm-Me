package leegroup.module.sample.gituser.domain.usecases.gituser

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import leegroup.module.sample.gituser.domain.MockUtil
import leegroup.module.sample.gituser.domain.repositories.GitUserDetailRepository
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetGitUserDetailLocalUseCaseTest {

    private lateinit var mockRepository: GitUserDetailRepository
    private lateinit var useCase: GetGitUserDetailLocalUseCase

    private val login = MockUtil.GIT_USER_DETAIL_LOGIN
    private val gitUserDetail = MockUtil.gitUserDetail

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetGitUserDetailLocalUseCase(mockRepository)
    }

    @Test
    fun `When request successful, it returns success`() = runTest {
        val expected = gitUserDetail
        coEvery { mockRepository.getLocal(login) } returns expected

        useCase(login).collect {
            it shouldBe expected
        }
    }

    @Test
    fun `When request return null, it returns nothing`() = runTest {
        coEvery { mockRepository.getLocal(login) } returns null

        useCase(login).firstOrNull() shouldBe null
    }

    @Test
    fun `When request failed, it returns error`() = runTest {
        val expected = RuntimeException()
        coEvery { mockRepository.getLocal(login) } throws expected

        useCase(login).catch {
            it shouldBe expected
        }.collect()
    }
}