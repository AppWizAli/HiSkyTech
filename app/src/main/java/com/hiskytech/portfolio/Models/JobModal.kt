package com.hiskytech.portfolio.Models

import com.google.firebase.Timestamp

data class JobModal(
    var title:String ="",
    var description:String ="",
    var sallary:String = "",
    var companyName:String = "",
    var location:String = "",
    var docID:String="",
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)
