package leegroup.module.sample.gituser.domain.usecases.gituser

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.domain.params.GetGitUserListParam
import leegroup.module.sample.gituser.domain.repositories.GitUserRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetGitUserUseCaseTest {

    private lateinit var repository: GitUserRepository
    private lateinit var getGitUserUseCase: GetGitUserUseCase

    private val gitUserModels = listOf(
        GitUserModel(
            1,
            "longdt57",
            "https://avatars.githubusercontent.com/u/8809113?v=4",
            "https://github.com/longdt57"
        ),
        GitUserModel(2, "user2", "avatar2", "htmlUrl2")
    )

    private val param = GetGitUserListParam(since = 0, perPage = 2)

    @Before
    fun setUp() {
        repository = mockk()
        getGitUserUseCase = GetGitUserUseCase(repository)
    }

    @Test
    fun `should return local data when local is not empty`() = runTest {
        coEvery { repository.getLocal(param) } returns gitUserModels

        val result = getGitUserUseCase(param).first()

        assertEquals(gitUserModels, result)
        coVerify(exactly = 1) { repository.getLocal(param) }
        coVerify(exactly = 0) { repository.getRemote(param) }
    }

    @Test
    fun `should fetch remote data when local is empty`() = runTest {
        coEvery { repository.getLocal(param) } returns emptyList()
        coEvery { repository.getRemote(param) } returns gitUserModels

        val result = getGitUserUseCase(param).first()

        assertEquals(gitUserModels, result)
        coVerify(exactly = 1) { repository.getLocal(param) }
        coVerify(exactly = 1) { repository.getRemote(param) }
    }
}
