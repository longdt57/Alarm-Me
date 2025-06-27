package leegroup.module.sample.gituser.ui.mapper

import android.content.Context
import leegroup.module.sample.gituser.domain.models.GitUserDetailModel
import leegroup.module.sample.gituser.support.extensions.stringNotSet
import leegroup.module.sample.gituser.ui.mapper.util.FollowerFormatter
import leegroup.module.sample.gituser.ui.models.GitUserDetailUiModel
import javax.inject.Inject

internal interface GitUserDetailUiMapper {
    fun mapToUiModel(
        oldUiModel: GitUserDetailUiModel,
        model: GitUserDetailModel
    ): GitUserDetailUiModel
}

internal class GitUserDetailUiMapperImpl @Inject constructor(
    private val context: Context
) : GitUserDetailUiMapper {

    override fun mapToUiModel(
        oldUiModel: GitUserDetailUiModel,
        model: GitUserDetailModel
    ): GitUserDetailUiModel {
        return oldUiModel.copy(
            name = model.name ?: model.login,
            avatarUrl = model.avatarUrl.orEmpty(),
            blog = model.blog.orEmpty(),
            location = model.location.takeUnless { it.isNullOrBlank() } ?: context.stringNotSet(),
            followers = FollowerFormatter.formatLargeNumber(model.followers),
            following = FollowerFormatter.formatLargeNumber(model.following),
        )
    }
}