package com.hiskytech.portfolio.Models

import com.google.firebase.Timestamp
import com.google.gson.Gson

data class TeamModal(
    var member_name:String="",
    var member_email: String = "",
    var member_Phone: String = "",
    var member_address: String="",
    var userId: String = "",
    var thumnail:String="",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)
{
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        fun fromString(modelFA: String): TeamModal? {
            val gson = Gson()
            return try {
                gson.fromJson(modelFA, TeamModal::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}
