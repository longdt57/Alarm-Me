package leegroup.module.sample.gituser.di.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import leegroup.module.sample.gituser.data.repositories.GitUserDetailRepositoryImpl
import leegroup.module.sample.gituser.data.repositories.GitUserPreferencesRepositoryImpl
import leegroup.module.sample.gituser.data.repositories.GitUserRepositoryImpl
import leegroup.module.sample.gituser.domain.repositories.GitUserDetailRepository
import leegroup.module.sample.gituser.domain.repositories.GitUserPreferencesRepository
import leegroup.module.sample.gituser.domain.repositories.GitUserRepository

@Module
@InstallIn(ViewModelComponent::class)
internal interface GitUserRepositoryModule {

    @Binds
    fun bindGitUserRepository(repository: GitUserRepositoryImpl): GitUserRepository

    @Binds
    fun bindGitUserDetailRepository(repository: GitUserDetailRepositoryImpl): GitUserDetailRepository

    @Binds
    fun bindAppPreferencesRepository(
        appPreferencesRepositoryImpl: GitUserPreferencesRepositoryImpl
    ): GitUserPreferencesRepository
}