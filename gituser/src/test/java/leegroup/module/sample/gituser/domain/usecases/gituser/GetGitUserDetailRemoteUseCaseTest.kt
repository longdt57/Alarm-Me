package leegroup.module.sample.gituser.domain.usecases.gituser

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import leegroup.module.sample.gituser.domain.MockUtil
import leegroup.module.sample.gituser.domain.repositories.GitUserDetailRepository
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetGitUserDetailRemoteUseCaseTest {

    private lateinit var mockRepository: GitUserDetailRepository
    private lateinit var useCase: GetGitUserDetailRemoteUseCase

    private val login = MockUtil.GIT_USER_DETAIL_LOGIN
    private val gitUserDetail = MockUtil.gitUserDetail

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetGitUserDetailRemoteUseCase(mockRepository)
    }

    @Test
    fun `When request successful, it returns success`() = runTest {
        val expected = gitUserDetail
        coEvery { mockRepository.getRemote(login) } returns expected

        useCase(login).collect {
            it shouldBe expected
        }
    }

    @Test
    fun `When request failed, it returns error`() = runTest {
        val expected = MockUtil.apiException
        coEvery { mockRepository.getRemote(login) } throws expected

        useCase(login).catch {
            it shouldBe expected
        }.collect()
    }
}