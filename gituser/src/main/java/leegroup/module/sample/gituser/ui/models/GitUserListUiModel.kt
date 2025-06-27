package leegroup.module.sample.gituser.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import leegroup.module.sample.gituser.domain.models.GitUserModel

@Immutable
internal data class GitUserListUiModel(
    val users: ImmutableList<GitUserModel> = persistentListOf(),
)
