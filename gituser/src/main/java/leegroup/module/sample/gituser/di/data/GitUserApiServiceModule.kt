package leegroup.module.sample.gituser.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.sample.gituser.data.remote.services.GitUserApiService
import leegroup.module.sample.gituser.data.remote.services.GitUserApiServiceImpl

@Module
@InstallIn(SingletonComponent::class)
internal class GitUserApiServiceModule {

//    @Provides
//    fun provideService(@GitUserRetrofit retrofit: Retrofit): GitUserApiService {
//        return retrofit.create(GitUserApiService::class.java)
//    }


    @Provides
    fun provideService(gitUserApiServiceImpl: GitUserApiServiceImpl): GitUserApiService {
        return gitUserApiServiceImpl
    }
}