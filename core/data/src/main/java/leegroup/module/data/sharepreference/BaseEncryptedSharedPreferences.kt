package leegroup.module.data.sharepreference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

abstract class BaseEncryptedSharedPreferences(
    applicationContext: Context,
    prefName: String
) : BaseSharedPreferences(applicationContext, prefName = prefName) {

    override val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            prefName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}