package leegroup.module.data.provider

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClientProvider {

    private const val NETWORK_TIMEOUT = 30L

    fun provideOkHttpClientWithInterceptor(
        isLoggingEnable: Boolean,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(provideLoggingInterceptor(isLoggingEnable))

            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            callTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    private fun provideLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when (isDebug) {
                true -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}