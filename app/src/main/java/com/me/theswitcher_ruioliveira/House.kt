package com.me.theswitcher_ruioliveira

import com.google.gson.Gson

data class House(val divisions: ArrayList<Division>) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
