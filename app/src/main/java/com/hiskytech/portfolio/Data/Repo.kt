package com.hiskytech.portfolio.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.Models.JobModal
import com.hiskytech.portfolio.ViewModels.CourseViewModal

class Repo(var context: CourseViewModal) {


    private var constants = Constants()


    ///////////////////////////   FIREBASE    //////////////////////////////////
    private val db = Firebase.firestore
    private val firebaseStorage = Firebase.storage
    private val storageRef = firebaseStorage.reference
    private var courseCollection= db.collection(constants.Course_Collection)
    private var UserCollection = db.collection(constants.USER_COLLECTION)
    private var annoucementCollection = db.collection(constants.ANNOUCEMENT_COLLECTION)
    private var jobCollection  = db.collection(constants.JOB_COLLECTION)
    private var AdminCollection = db.collection(constants.ADMIN_COLLECTION)
    private var DramaCollection = db.collection(constants.DRAMA_COLLECTION)
    private var SeasonCollection = db.collection(constants.SEASON_COLLECTION)
    private var VideoManagementCollection = db.collection(constants.VIDEOMANAGEMENT_COLLECTION)





    fun getUserList():Task<QuerySnapshot>
    {
        return UserCollection.get()
    }
    fun add_course(course_modal:CourseModal):LiveData<Boolean>
    {
        var result = MutableLiveData<Boolean>()
       courseCollection.add(course_modal).addOnSuccessListener()
        {document->
            course_modal.docID = document.id

            db.collection(constants.Course_Collection).document(document.id).set(course_modal)
                .addOnSuccessListener()
                {
                    result.value = true
                }.addOnFailureListener()
                {e->
                    result.value = false
                }

        }.addOnFailureListener()
        {
            result.value = false
        }
    return result}

 fun getCourseList(): Task<QuerySnapshot> {
       return courseCollection.get()
    }

    fun deleteDrama(coursemodal: CourseModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        courseCollection.document(coursemodal.docID).delete()
            .addOnSuccessListener {
                result.value = true
                // Deletion successful, handle any success cases if needed
            }
            .addOnFailureListener {
                result.value = false
                // Handle failure scenarios if needed
            }
        return result
    }

    fun updateCourse(courseModal: CourseModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        courseCollection.document(courseModal.docID).set(courseModal)
            .addOnSuccessListener {
                result.value = true
                // Update successful, handle any success cases if needed
            }
            .addOnFailureListener {
                result.value = false

                // Handle failure scenarios if needed
            }
        return result

    }

    fun add_Annoucement(annoucementModal: AnnoucementModal): LiveData<Boolean> {

        var result = MutableLiveData<Boolean>()
        annoucementCollection.add(annoucementModal).addOnSuccessListener()
        {document->
            annoucementModal.docID = document.id

            db.collection(annoucementCollection.toString()).document(document.id).set(annoucementModal)
                .addOnSuccessListener()
                {
                    result.value = true
                }.addOnFailureListener()
                {e->
                    result.value = false
                }

        }.addOnFailureListener()
        {
            result.value = false
        }
        return result
    }
    fun add_Job(jobModal: JobModal): LiveData<Boolean> {

        var result = MutableLiveData<Boolean>()
        jobCollection.add(jobModal).addOnSuccessListener()
        {document->
            jobModal.docID = document.id

            jobCollection.document(document.id).set(jobModal)
                .addOnSuccessListener()
                {
                    result.value = true
                }.addOnFailureListener()
                {e->
                    result.value = false
                }

        }.addOnFailureListener()
        {
            result.value = false
        }
        return result
    }

    fun get_job_list(): Task<QuerySnapshot> {
return jobCollection.get()
    }

    fun updatejob(jobModal: JobModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        jobCollection.document(jobModal.docID).set(jobModal)
            .addOnSuccessListener {
                result.value = true
                // Update successful, handle any success cases if needed
            }
            .addOnFailureListener {
                result.value = false

                // Handle failure scenarios if needed
            }
        return result

    }

    fun deleteJob(jobModal: JobModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        jobCollection.document(jobModal.docID).delete()
            .addOnSuccessListener {
                result.value = true
                // Deletion successful, handle any success cases if needed
            }
            .addOnFailureListener {
                result.value = false
                // Handle failure scenarios if needed
            }
        return result
    }

    fun get_Annoucement_list(): Task<QuerySnapshot> {

        return annoucementCollection.get()
    }


}