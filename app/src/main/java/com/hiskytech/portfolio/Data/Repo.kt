package com.hiskytech.portfolio.Data

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.ViewModels.UserViewModal

class Repo(var context: Context) {


    private var constants = Constants()
    private val sharedPrefManager = SharedPrefManager(context)


    ///////////////////////////   FIREBASE    //////////////////////////////////
    private val db = Firebase.firestore
    private val firebaseStorage = Firebase.storage
    private val storageRef = firebaseStorage.reference

    private var VideoCollection = db.collection(constants.VIDEO_COLLECTION)
    private var UserCollection = db.collection(constants.USER_COLLECTION)
    private var BannerCollection = db.collection(constants.BANNER_COLLECTION)
    private var GroupCollection = db.collection(constants.GROUP_COLLECTION)
    private var AdminCollection = db.collection(constants.ADMIN_COLLECTION)
    private var DramaCollection = db.collection(constants.DRAMA_COLLECTION)
    private var SeasonCollection = db.collection(constants.SEASON_COLLECTION)
    private var VideoManagementCollection = db.collection(constants.VIDEOMANAGEMENT_COLLECTION)





    fun getUserList():Task<QuerySnapshot>
    {
        return UserCollection.get()
    }
/*

    suspend fun addVideo(modelVideo: ModelVideo): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        VideoCollection.add(modelVideo)
            .addOnSuccessListener { documentReference ->
                // Store the generated document ID in the ModelDrama
                modelVideo.docId = documentReference.id

                // Update the document with the stored ID
                VideoCollection.document(documentReference.id).set(modelVideo)
                    .addOnSuccessListener {
                        result.value = true
                    }
                    .addOnFailureListener {
                        result.value = false
                    }
            }
            .addOnFailureListener {
                result.value = false
            }

        return result
    }
*/

/*
    suspend fun getAssignedVideo(userIds:String): Task<QuerySnapshot> {
        return VideoCollection
            .whereEqualTo(constants.PRIVACY, constants.VIDEO_PRIVACY_PRIVATE)
            .whereEqualTo("users_Id", userIds)
            .get()
    }*/



    }