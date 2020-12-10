package com.me.theswitcher_ruioliveira

import android.content.Context
import android.content.SharedPreferences

class Utils {

    companion object {
        const val PACKAGE = "com.me.theswitcher_ruioliveira"
        const val HOUSE_DIVISIONS = "HouseDivisions"
        private const val defaultValue = ""
        private const val defaultValueArray = "{ \"array\": [ ] }"

        /**
         * @param key
         * @return
         */
        fun getStringFromShared(context: Context, key: String?): String? {
            val preferences = context.getSharedPreferences(
                "$PACKAGE.$key", Context.MODE_PRIVATE)

            // get values from Map
            return preferences.getString(key, defaultValue)
        }

        /**
         * @param key
         * @param value
         */
        fun setStringFromShared(context: Context, key: String?, value: String?) {
            val preferences = context.getSharedPreferences(
                "$PACKAGE.$key", Context.MODE_PRIVATE)

            with (preferences.edit()) {
                putString(key, value)
                commit()
            }
        }

        fun contains(context: Context, key: String?): Boolean? {
            val preferences = context.getSharedPreferences(
                "$PACKAGE.$key", Context.MODE_PRIVATE) ?: return false

            return preferences.contains(key)
        }
    }
}