package leegroup.module.sample.gituser.data.local.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.data.sharepreference.BaseEncryptedSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

private const val APP_SECRET_SHARED_PREFS = "app_secret_shared_prefs"

@Singleton
internal class GitUserEncryptedSharedPreferences @Inject constructor(
    @ApplicationContext applicationContext: Context,
) :
    BaseEncryptedSharedPreferences(applicationContext, prefName = APP_SECRET_SHARED_PREFS) {

    var accessToken: String? by argsNullable("access_token")
}
