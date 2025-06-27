package leegroup.module.data.sharepreference

import android.content.SharedPreferences
import kotlin.reflect.KProperty

fun SharedPreferences.execute(operation: (SharedPreferences.Editor) -> Unit) {
    with(edit()) {
        operation(this)
        apply()
    }
}

fun KProperty<*>.getKey(key: String?): String {
    return key ?: name
}

inline fun <reified T> SharedPreferences.get(key: String): T? =
    if (this.contains(key)) {
        when (T::class) {
            Boolean::class -> this.getBoolean(key, false) as T?
            String::class -> this.getString(key, null) as T?
            Float::class -> this.getFloat(key, 0f) as T?
            Int::class -> this.getInt(key, 0) as T?
            Long::class -> this.getLong(key, 0L) as T?
            Set::class -> this.getStringSet(key, null) as T?
            else -> error("Illegal type: ${T::class}")
        }
    } else {
        null
    }

fun SharedPreferences.set(key: String, value: Any?) {
    this.execute {
        if (value == null) {
            it.remove(key)
            return@execute
        }
        when (value) {
            is Boolean -> it.putBoolean(key, value)
            is String -> it.putString(key, value)
            is Float -> it.putFloat(key, value)
            is Long -> it.putLong(key, value)
            is Int -> it.putInt(key, value)
            is Set<*> -> it.putStringSet(key, value as Set<String>)
            else -> error("Illegal item: $value")
        }
    }
}

