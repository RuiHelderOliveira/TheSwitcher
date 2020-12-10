package com.me.theswitcher_ruioliveira

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.io.InputStream

class HouseViewModel(application: Application) : AndroidViewModel(application) {

    private val _houseResult = MutableLiveData<House>()
    val houseResultLiveData = _houseResult
    private lateinit var divisions: House

    private val context = application.applicationContext

    fun loadDivisions() {

        var json: String? = null
        val alreadyExist = Utils.contains(context, Utils.HOUSE_DIVISIONS)
        if (alreadyExist == true) {
            json = Utils.getStringFromShared(context, Utils.HOUSE_DIVISIONS)
        } else {
            try {
                val inputStream: InputStream = context.assets.open("house.json")
                json = inputStream.bufferedReader().use { it.readText() }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        val gson = Gson()
        divisions = gson.fromJson(json, House::class.java)

        _houseResult.postValue(divisions)

        if (alreadyExist == false)
            Utils.setStringFromShared(context, Utils.HOUSE_DIVISIONS, divisions.toString())
    }

    fun ChangeItem(division: Division, checked: Boolean) {
        val position: Int = divisions.divisions.indexOf(division)
        divisions.divisions[position].state = checked
        Utils.setStringFromShared(context, Utils.HOUSE_DIVISIONS, divisions.toString())
        _houseResult.postValue(divisions)
    }

    fun addItem(division: String) {

        divisions.divisions.add(Division(division, false))
        Utils.setStringFromShared(context, Utils.HOUSE_DIVISIONS, divisions.toString())
        _houseResult.postValue(divisions)
    }

    fun deleteItem(division: Division) {

        divisions.divisions.remove(division)
        Utils.setStringFromShared(context, Utils.HOUSE_DIVISIONS, divisions.toString())
        _houseResult.postValue(divisions)
    }
}