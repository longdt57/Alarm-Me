package leegroup.module.photosample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.data.provider.RetrofitProvider
import leegroup.module.photosample.BuildConfig
import leegroup.module.photosample.data.remote.services.PhotoApiService
import retrofit2.Retrofit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
internal class PhotoRetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class PhotoRetrofit

    @PhotoRetrofit
    @Provides
    fun provideAppRetrofit(): Retrofit {
        return RetrofitProvider.provideAppRetrofit(
            isLoggingEnable = BuildConfig.DEBUG,
            baseUrl = BuildConfig.BASE_API_URL
        )
    }

    @Provides
    fun provideService(@PhotoRetrofit retrofit: Retrofit): PhotoApiService {
        return retrofit.create(PhotoApiService::class.java)
    }
}
