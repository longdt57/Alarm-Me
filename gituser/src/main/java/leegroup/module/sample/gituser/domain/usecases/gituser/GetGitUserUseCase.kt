package leegroup.module.sample.gituser.domain.usecases.gituser

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.domain.params.GetGitUserListParam
import leegroup.module.sample.gituser.domain.repositories.GitUserRepository
import javax.inject.Inject

internal class GetGitUserUseCase @Inject constructor(
    private val repository: GitUserRepository
) {

    operator fun invoke(param: GetGitUserListParam): Flow<List<GitUserModel>> {
        return flow {
            val localData = repository.getLocal(param)
            if (localData.isNotEmpty()) {
                emit(localData)
            } else {
                emit(repository.getRemote(param))
            }
        }
    }
}