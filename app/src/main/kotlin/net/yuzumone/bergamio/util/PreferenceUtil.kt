package net.yuzumone.bergamio.util

import android.content.Context

class PreferenceUtil(context: Context) {

    private val filename = "net.yuzumone.bergamio.pref"
    private val preference = context.getSharedPreferences(filename, 0)
    private val TOKEN = "token"
    private val EXPIRE = "expire"

    var token: String
    get() = preference.getString(TOKEN, null)
    set(value) = preference.edit().putString(TOKEN, value).apply()

    val hasAvailableToken: Boolean
    get() {
        if (token == "" && expire == 0L) {
            return false
        }
        val now = System.currentTimeMillis() / 1000L
        return (now < expire) && (token != "")
    }

    var expire: Long
    get() = preference.getLong(EXPIRE, 0L)
    set(value) {
        val now = System.currentTimeMillis() / 1000L
        val tile = now + value
        preference.edit().putLong(EXPIRE, tile).apply()
    }
}