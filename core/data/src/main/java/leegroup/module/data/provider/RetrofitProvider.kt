package leegroup.module.data.provider

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import leegroup.module.core.util.JsonUtil
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit

object RetrofitProvider {

    private fun provideConverterFactory(): Converter.Factory {
        return JsonUtil.json.asConverterFactory(
            "application/json".toMediaType()
        )
    }

    fun provideAppRetrofit(
        isLoggingEnable: Boolean,
        baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClientProvider.provideOkHttpClientWithInterceptor(isLoggingEnable)
            )
            .addConverterFactory(provideConverterFactory())
            .build()
    }

}