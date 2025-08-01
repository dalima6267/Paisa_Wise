package com.dalima.paisawise

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object PinStorage {
    private const val PREF_NAME = "secure_pin_prefs"
    private const val PIN_KEY = "user_pin"

    private fun getSharedPrefs(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun savePin(context: Context, pin: String) {
        val prefs = getSharedPrefs(context)
        prefs.edit().putString(PIN_KEY, pin).apply()
    }

    fun getSavedPin(context: Context): String? {
        return getSharedPrefs(context).getString(PIN_KEY, null)
    }

    fun hasPin(context: Context): Boolean {
        return getSavedPin(context) != null
    }
}
