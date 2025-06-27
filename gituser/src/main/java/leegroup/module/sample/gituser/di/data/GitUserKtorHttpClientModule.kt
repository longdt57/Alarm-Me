package leegroup.module.sample.gituser.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import leegroup.module.data.provider.KtorHttpClientProvider
import leegroup.module.sample.gituser.BuildConfig
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
internal class GitUserKtorHttpClientModule {

    @Named(GIT_USER_KTOR_HTTP_CLIENT)
    @Provides
    fun provideGitUserKtorHttpClient(): HttpClient {
        return KtorHttpClientProvider.provideHttpClient(
            isLoggingEnable = BuildConfig.DEBUG,
            configs = {},
            block = {
                url(BuildConfig.BASE_API_URL)
            }
        )
    }

    companion object {
        const val GIT_USER_KTOR_HTTP_CLIENT = "gitUserKtorHttpClient"
    }
}