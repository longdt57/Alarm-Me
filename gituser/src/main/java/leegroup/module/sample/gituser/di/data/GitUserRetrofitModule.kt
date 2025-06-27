package leegroup.module.sample.gituser.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.data.provider.RetrofitProvider
import leegroup.module.sample.gituser.BuildConfig
import retrofit2.Retrofit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
internal class GitUserRetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GitUserRetrofit

    @GitUserRetrofit
    @Provides
    fun provideAppRetrofit(): Retrofit {
        return RetrofitProvider.provideAppRetrofit(
            isLoggingEnable = BuildConfig.DEBUG,
            baseUrl = BuildConfig.BASE_API_URL
        )
    }
}
