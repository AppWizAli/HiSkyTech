package com.hiskytech.portfolio.Data

import android.content.Context
import android.content.SharedPreferences
import com.hiskytech.portfolio.ViewModels.UserViewModal

class SharedPrefManager(var context: Context) {


    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()



/*    fun getDramaList(): List<ModelDrama>{

        val json = sharedPref.getString("ListDrama", "") ?: ""
        val type: Type = object : TypeToken<List<ModelDrama?>?>() {}.getType()
        return Gson().fromJson(json, type)
    }

    fun putDramaList(list: List<ModelDrama>) {
        editor.putString("ListDrama", Gson().toJson(list))
        editor.commit()
    }*/

    public fun clearWholeSharedPrefrences()

    {
        editor.clear()
        editor.commit()
    }
}