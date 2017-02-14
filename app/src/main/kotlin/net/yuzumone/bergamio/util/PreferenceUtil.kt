package net.yuzumone.bergamio.util

import android.content.Context
import android.preference.PreferenceManager

class PreferenceUtil {
    companion object {
        private val TOKEN = "token"
        private val EXPIRE = "expire"
        fun loadToken(context: Context): String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val token  = preferences.getString(TOKEN, null)
            if (token == null) {
                return ""
            } else {
                return token
            }
        }
        fun storeToken(context: Context, token: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(TOKEN, token)
            editor.apply()
        }
        fun loadExpire(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val expire  = preferences.getLong(EXPIRE, 0L)
            if (expire == 0L) {
                return 0L
            } else {
                return expire
            }
        }
        fun storeExpire(context: Context, expire: String) {
            val now = System.currentTimeMillis() / 1000L
            val expireTime = now + expire.toLong()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putLong(EXPIRE, expireTime)
            editor.apply()
        }
        fun hasAvailableToken(context: Context): Boolean {
            val token = loadToken(context)
            val expire = loadExpire(context)
            if (token == "" && expire == 0L) {
                return false
            }
            val now = System.currentTimeMillis() / 1000L
            return (now < expire) && (token != "")
        }
    }
}