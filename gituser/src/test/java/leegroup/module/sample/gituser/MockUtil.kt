package leegroup.module.sample.gituser

import leegroup.module.designsystem.ui.models.ErrorState
import java.net.UnknownHostException

object MockUtil {
    val noConnectivityException: Throwable = UnknownHostException()
    val apiErrorState = ErrorState.Api()
}