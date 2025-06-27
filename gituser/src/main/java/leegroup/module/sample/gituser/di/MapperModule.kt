package leegroup.module.sample.gituser.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import leegroup.module.sample.gituser.ui.mapper.GitUserDetailUiMapper
import leegroup.module.sample.gituser.ui.mapper.GitUserDetailUiMapperImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface MapperModule {
    @Binds
    fun bindGitUserDetailUiMapper(mapper: GitUserDetailUiMapperImpl): GitUserDetailUiMapper
}