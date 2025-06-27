package leegroup.module.sample.gituser.support.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import leegroup.module.sample.gituser.R
import timber.log.Timber

internal fun Context.stringNotSet() = getString(R.string.not_set)

internal fun Context.formatAndOpenUrl(url: String) {
    val formattedUrl = url.formattedUrl()
    if (formattedUrl.isNotBlank() && Patterns.WEB_URL.matcher(formattedUrl).matches()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
        startActivity(intent)
    } else {
        Timber.d("Invalid URL: $url")
    }
}
