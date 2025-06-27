package leegroup.module.core.extensions

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle

fun Intent.getExtraStreamUri(): Uri? = try {
    extras?.getParcelable(Intent.EXTRA_STREAM)
} catch (ex: Exception) {
    Log.e("getExtraStreamUri", ex.message.orEmpty())
    null
}

fun Intent.clearExtraStreamUri() {
    removeExtra(Intent.EXTRA_STREAM)
}

inline fun <reified T : Parcelable> Intent.getParcelable(key: String = T::class.java.simpleName): T? {
    return if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        getParcelableExtra(key)
    }
}

inline fun <reified T : Parcelable> Intent.putParcelable(
    value: T,
    key: String = T::class.java.simpleName
) {
    putExtra(key, value)
}

inline fun <reified T : Parcelable> SavedStateHandle.getParcelable(
    key: String = T::class.java.simpleName
): T? {
    return get<T>(key)
}
