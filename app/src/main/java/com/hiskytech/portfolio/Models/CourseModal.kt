package com.hiskytech.portfolio.Models

import com.google.firebase.Timestamp
import com.google.gson.Gson

data class CourseModal(
    var title:String ="",
    var description:String="",
    var language :String="",
    var docID:String="",
    var thumbnail:String="",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)
{
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        fun fromString(modelFA: String): CourseModal? {
            val gson = Gson()
            return try {
                gson.fromJson(modelFA, CourseModal::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}
