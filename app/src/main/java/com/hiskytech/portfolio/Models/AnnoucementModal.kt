package com.hiskytech.portfolio.Models

import android.app.appsearch.util.DocumentIdUtil
import com.google.gson.Gson

data class AnnoucementModal(
    var thumnail:String = "" ,
    var thumnailSecond: String ="",
    var title:String = "",
    var description:String ="",
    var docID:String=""
){
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        fun fromString(modelFA: String): AnnoucementModal? {
            val gson = Gson()
            return try {
                gson.fromJson(modelFA, AnnoucementModal::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}
