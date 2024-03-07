package com.hiskytech.portfolio.Models

import com.google.firebase.Timestamp
import com.google.gson.Gson

data class CompletedprojectModal(
    var project_title:String ="",
    var project_description:String ="",
    var project_duration: String ="",
    var client_feedback:String ="",
    var acievement:String="",
    var docId:String= "",
    var thumnail:String ="",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp

){
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        fun fromString(modelFA: String): CompletedprojectModal? {
            val gson = Gson()
            return try {
                gson.fromJson(modelFA, CompletedprojectModal::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}
