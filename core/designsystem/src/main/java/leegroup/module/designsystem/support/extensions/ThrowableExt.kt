package leegroup.module.designsystem.support.extensions

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import leegroup.module.core.util.JsonUtil
import leegroup.module.designsystem.ui.models.ErrorModel
import leegroup.module.designsystem.ui.models.ErrorState
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLException


suspend inline fun <reified Model : ErrorModel> Throwable.mapApiError(): ErrorState {
    return when (this) {
        is UnknownHostException,
        is SSLException,
        is InterruptedIOException -> ErrorState.Network

        is ConnectException -> ErrorState.Server
        is HttpException -> {
            ErrorState.Api(
                JsonUtil.decodeFromString<Model>(response()?.errorBody()?.string().orEmpty())
            )
        }

        is ClientRequestException -> ErrorState.Api(error = response.body<Model>())
        else -> ErrorState.Common
    }
}
