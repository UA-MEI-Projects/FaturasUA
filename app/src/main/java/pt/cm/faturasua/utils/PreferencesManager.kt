package pt.cm.faturasua.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(
    context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue) ?: defaultValue
    }

    companion object{
        val PREFERENCE_KEY : String = "FaturasUAPrefs"
        val PREFERENCE_CODE_DARK_MODE: String = "DarkModeKeyPref"
        val PREFERENCE_CODE_NOTIFICATIONS: String = "NotifPref"
        val PREFERENCE_CODE_LANGUAGE: String = "LanguagePref"
    }
}