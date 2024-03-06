package com.hiskytech.portfolio.Models

import com.google.firebase.Timestamp
import com.google.gson.Gson

data class Usermodel(
    var name:String="",
    var email: String = "",
    var password: String = "",
    var userId: String = "",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)
{
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        fun fromString(modelFA: String): ModelUser? {
            val gson = Gson()
            return try {
                gson.fromJson(modelFA, ModelUser::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}