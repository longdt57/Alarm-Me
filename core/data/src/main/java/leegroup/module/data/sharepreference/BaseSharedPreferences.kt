package leegroup.module.data.sharepreference

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class BaseSharedPreferences(
    applicationContext: Context,
    prefName: String
) {

    protected open val sharedPreferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    protected inline fun <reified T> argsNullable(
        key: String? = null,
        defaultValue: T? = null
    ): ReadWriteProperty<Any, T?> {
        return object : ReadWriteProperty<Any, T?> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = sharedPreferences.get(property.getKey(key)) ?: defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: T?
            ) {
                sharedPreferences.set(property.getKey(key), value)
            }
        }
    }

    protected inline fun <reified T> args(
        key: String? = null,
        defaultValue: T
    ): ReadWriteProperty<Any, T> {
        return object : ReadWriteProperty<Any, T> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = sharedPreferences.get(property.getKey(key)) ?: defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: T
            ) {
                sharedPreferences.set(property.getKey(key), value)
            }
        }
    }

    protected fun remove(key: String) {
        sharedPreferences.execute { it.remove(key) }
    }

    protected fun clearAll() {
        sharedPreferences.execute { it.clear() }
    }
}
