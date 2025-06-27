package leegroup.module.sample.gituser.ui.screen.gituser

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import leegroup.module.designsystem.ui.models.ErrorState
import leegroup.module.designsystem.ui.models.LoadingState
import leegroup.module.sample.gituser.MockUtil
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.domain.params.GetGitUserListParam
import leegroup.module.sample.gituser.domain.usecases.gituser.GetGitUserUseCase
import leegroup.module.sample.gituser.tracking.GitUserListScreenTracker
import leegroup.module.sample.gituser.ui.screens.gituser.GitUserListAction
import leegroup.module.sample.gituser.ui.screens.gituser.GitUserListViewModel
import leegroup.module.sample.gituser.ui.screens.gituser.GitUserListViewModel.Companion.PER_PAGE
import leegroup.module.test.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GitUserListViewModelTest : BaseUnitTest() {

    private val mockUseCase: GetGitUserUseCase = mockk()
    private val mockTracker: GitUserListScreenTracker = mockk()

    private lateinit var viewModel: GitUserListViewModel

    private val param = GetGitUserListParam(0, PER_PAGE)

    private val gitUserModels = listOf(
        GitUserModel(
            id = 1,
            login = "longdt57",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            htmlUrl = "https://github.com/longdt57"
        ),
        GitUserModel(
            id = 2,
            login = "defunkt",
            avatarUrl = "https://avatars.githubusercontent.com/u/2?v=4",
            htmlUrl = "https://github.com/defunkt"
        ),
        GitUserModel(
            id = 3,
            login = "pjhyett",
            avatarUrl = "https://avatars.githubusercontent.com/u/3?v=4",
            htmlUrl = "https://github.com/pjhyett"
        )
    )

    @Before
    fun setUp() {
        every { mockUseCase(param) } returns flowOf(gitUserModels)
        every { mockTracker.launch() } returns Unit
        every { mockTracker.openUserDetail(any()) } returns Unit
        viewModel = GitUserListViewModel(
            testDispatcherProvider,
            useCase = mockUseCase,
            gitUserListScreenTracker = mockTracker
        )
    }

    @Test
    fun `When trigger load if empty & state is empty, it should load more`() = runTest {
        viewModel.handleAction(GitUserListAction.LoadIfEmpty)
        verify(exactly = 1) { mockUseCase(param) }
    }

    @Test
    fun `When track launch, trigger tracker`() = runTest {
        viewModel.handleAction(GitUserListAction.TrackLaunch)
        verify(exactly = 1) { mockTracker.launch() }
    }

    @Test
    fun `When track open user detail, trigger tracker`() = runTest {
        val login = "longdt57"
        viewModel.handleAction(GitUserListAction.TrackOpenUserDetail(login))
        verify(exactly = 1) { mockTracker.openUserDetail(login) }
    }

    @Test
    fun `When trigger load if empty & state is not empty, it should not load more`() = runTest {
        viewModel.handleAction(GitUserListAction.LoadMore)
        viewModel.handleAction(GitUserListAction.LoadIfEmpty)
        verify(exactly = 1) { mockUseCase(param) }
    }

    @Test
    fun `When loading users successfully, it shows the user list`() = runTest {
        viewModel.handleAction(GitUserListAction.LoadMore)
        viewModel.uiState.test {
            val updated = expectMostRecentItem()
            assertEquals(
                "Expected updated users to match gitUserModels",
                gitUserModels,
                updated.users
            )
        }
        verify(exactly = 1) { mockUseCase(param) }
    }

    @Test
    fun `When loading users failed, it shows the corresponding error`() = runTest {
        val error = RuntimeException("Network error")
        every { mockUseCase(param) } returns flow { throw error }

        viewModel.handleAction(GitUserListAction.LoadMore)
        viewModel.error.test {
            val errorState = expectMostRecentItem()
            assertEquals("Expected error state to be UnknownError", ErrorState.Common, errorState)
        }
    }

    @Test
    fun `When loading users, it shows and hides loading correctly`() = runTest {
        every { mockUseCase(param) } returns flow {
            delay(1000)
            emit(gitUserModels)
        }
        viewModel.handleAction(GitUserListAction.LoadMore)
        viewModel.loading.test {
            val loading = expectMostRecentItem()
            assertEquals("Expected loading state to be Loading", LoadingState.Loading(), loading)
        }
    }

    @Test
    fun `When loading users, it can't call load more`() = runTest {
        every { mockUseCase(param) } returns flow {
            delay(1000)
            emit(gitUserModels)
        }
        viewModel.handleAction(GitUserListAction.LoadMore)
        viewModel.handleAction(GitUserListAction.LoadMore)
        verify(exactly = 1) { mockUseCase(param) }
    }

    @Test
    fun `When api onErrorConfirmation is called, it calls load if empty again`() = runTest {
        viewModel.onErrorConfirmation(MockUtil.apiErrorState)
        verify(exactly = 1) { mockUseCase(param) }
    }

    @Test
    fun `When network onErrorConfirmation is called, it calls load if empty again`() = runTest {
        viewModel.onErrorConfirmation(ErrorState.Network)
        verify(exactly = 1) { mockUseCase(param) }
    }

    @Test
    fun `When common onErrorConfirmation is called, it doesn't calls load if empty again`() =
        runTest {
            viewModel.onErrorConfirmation(ErrorState.Common)
            verify(exactly = 0) { mockUseCase(param) }
        }
}
