package leegroup.module.sample.gituser.tracking

import androidx.core.os.bundleOf
import leegroup.module.analytics.DefaultTracking
import leegroup.module.analytics.Tracking
import leegroup.module.analytics.event.AnalyticsEvent
import javax.inject.Inject

internal class GitUserListScreenTracker @Inject constructor(
    private val defaultTracking: DefaultTracking
) : Tracking by defaultTracking,
    Tracking.Launch {

    override val prefix: String get() = "MAIN_SCREEN"

    fun openUserDetail(login: String) {
        track(
            AnalyticsEvent(
                name = prefix + "open_user_detail",
                params = bundleOf(
                    "login" to login
                )
            )
        )
    }
}