package com.fortunatehtml.android.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var disclaimerAccepted: Boolean
        get() = prefs.getBoolean(KEY_DISCLAIMER_ACCEPTED, false)
        set(value) = prefs.edit().putBoolean(KEY_DISCLAIMER_ACCEPTED, value).apply()

    var proxyPort: Int
        get() = prefs.getInt(KEY_PROXY_PORT, DEFAULT_PROXY_PORT)
        set(value) = prefs.edit().putInt(KEY_PROXY_PORT, value).apply()

    var mitmEnabled: Boolean
        get() = prefs.getBoolean(KEY_MITM_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_MITM_ENABLED, value).apply()

    companion object {
        private const val PREFS_NAME = "fortunatehtml_prefs"
        private const val KEY_DISCLAIMER_ACCEPTED = "disclaimer_accepted"
        private const val KEY_PROXY_PORT = "proxy_port"
        private const val KEY_MITM_ENABLED = "mitm_enabled"
        const val DEFAULT_PROXY_PORT = 9090
    }
}
