package leegroup.module.sample.gituser.domain.usecases.gituser

import kotlinx.coroutines.flow.flow
import leegroup.module.sample.gituser.domain.repositories.GitUserDetailRepository
import javax.inject.Inject

internal class GetGitUserDetailLocalUseCase @Inject constructor(
    private val repository: GitUserDetailRepository
) {

    operator fun invoke(login: String) = flow {
        repository.getLocal(login)?.let {
            emit(it)
        }
    }
}
