package com.dalima.paisawise

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object PinStorage {
    private const val PREF_NAME = "secure_pin_prefs"
    private const val PIN_KEY = "user_pin"
    private const val TAGS_KEY = "selected_tags"
    private const val CATEGORY_NAME_KEY = "selected_category_name"
    private const val CATEGORY_ICON_KEY = "selected_category_icon"
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
    //Tag method
    fun saveSelectedTags(context: Context, tags: List<String>) {
        val prefs=getSharedPrefs(context)
        prefs.edit().putStringSet(TAGS_KEY, tags.toSet()).apply()
    }
    fun getSelectedTags(context: Context): List<String> {
        val prefs=getSharedPrefs(context)
        return prefs.getStringSet(TAGS_KEY, emptySet())?.toList() ?: emptyList()
    }
    fun clearSelectedTags(context: Context) {
        val prefs=getSharedPrefs(context)
        prefs.edit().remove(TAGS_KEY).apply()
    }
    fun saveSelectedCategory(context: Context, categoryName: String, iconRes: Int) {
        val prefs = getSharedPrefs(context)
        prefs.edit()
            .putString(CATEGORY_NAME_KEY, categoryName)
            .putInt(CATEGORY_ICON_KEY, iconRes)
            .apply()
    }

    fun getSelectedCategory(context: Context): Pair<String, Int>? {
        val prefs = getSharedPrefs(context)
        val name = prefs.getString(CATEGORY_NAME_KEY, null)
        val icon = prefs.getInt(CATEGORY_ICON_KEY, -1)
        return if (name != null && icon != -1) Pair(name, icon) else null
    }

    fun clearSelectedCategory(context: Context) {
        val prefs = getSharedPrefs(context)
        prefs.edit()
            .remove(CATEGORY_NAME_KEY)
            .remove(CATEGORY_ICON_KEY)
            .apply()
    }
}
