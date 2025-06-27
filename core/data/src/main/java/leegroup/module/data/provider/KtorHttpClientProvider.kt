package leegroup.module.data.provider

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import leegroup.module.core.util.JsonUtil

object KtorHttpClientProvider {

    fun provideHttpClient(
        isLoggingEnable: Boolean,
        configs: (HttpClientConfig<OkHttpConfig>) -> Unit,
        block: DefaultRequest.DefaultRequestBuilder.() -> Unit
    ): HttpClient {
        return HttpClient(OkHttp) {
            configs(this)
            install(ContentNegotiation) {
                json(JsonUtil.json)
            }

            if (isLoggingEnable) {
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.ALL // Các mức: NONE, HEADERS, BODY, ALL
                }
            }

            defaultRequest {
                block()
                header("Accept", "application/json")
            }
        }
    }
}