package leegroup.module.sample.gituser.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.core.util.SavedStateProvider
import leegroup.module.sample.gituser.ui.screens.GitUserDestination

@Module
@InstallIn(SingletonComponent::class)
class GitUserModule {

    @Provides
    fun provideGitUserSavedStateProvider(): SavedStateProvider<GitUserDestination.GitUserDetail.GitUserDetailNav> =
        object : SavedStateProvider<GitUserDestination.GitUserDetail.GitUserDetailNav> {

            override fun toRoute(savedStateHandle: SavedStateHandle): GitUserDestination.GitUserDetail.GitUserDetailNav {
                return savedStateHandle.toRoute()
            }
        }
}