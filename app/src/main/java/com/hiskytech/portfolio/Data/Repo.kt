package com.hiskytech.portfolio.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CompletedprojectModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.Models.JobModal
import com.hiskytech.portfolio.Models.TeamModal
import com.hiskytech.portfolio.Models.Usermodel
import com.hiskytech.portfolio.ViewModels.CourseViewModal

class Repo(var context: ViewModel) {


    private var constants = Constants()


    ///////////////////////////   FIREBASE    //////////////////////////////////
    private val db = Firebase.firestore
    private var courseCollection= db.collection(constants.Course_Collection)
    private var UserCollection = db.collection(constants.USER_COLLECTION)
    private var annoucementCollection = db.collection(constants.ANNOUCEMENT_COLLECTION)
    private var jobCollection  = db.collection(constants.JOB_COLLECTION)
    private var CompletedProjectCollection = db.collection(constants.COMPLETEDPROJECTS)
    private var TeamCollection  = db.collection(constants.TEAMCOLLECTION)


    ///////////////////////////  FUNCTIONS   //////////////////////////////////


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

    fun adduser(usermodel: Usermodel): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        // First, check if the email already exists
        db.collection("UserCollection")
            .whereEqualTo("email", usermodel.email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Email doesn't exist, proceed with adding the user
                    UserCollection.add(usermodel)
                        .addOnSuccessListener { documentReference ->
                            usermodel.userId = documentReference.id
                            // Update the document with the usermodel
                            UserCollection.document(documentReference.id)
                                .set(usermodel)
                                .addOnSuccessListener {
                                    result.value = true
                                }
                                .addOnFailureListener { e ->
                                    result.value = false
                                }
                        }
                        .addOnFailureListener {
                            result.value = false
                        }
                } else {
                    // Email already exists
                    result.value = false
                }
            }
            .addOnFailureListener {
                result.value = false
            }

        return result
    }


    fun get_data(): Task<QuerySnapshot> {
            return UserCollection.get()
    }

    fun add_completed_course(completedprojectModal: CompletedprojectModal): LiveData<Boolean> {

        var result = MutableLiveData<Boolean>()

        CompletedProjectCollection.add(completedprojectModal).addOnSuccessListener()
        {document->
            completedprojectModal.docId = document.id

            CompletedProjectCollection.document(document.id).set(completedprojectModal)
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
    fun get_complted_project_list(): Task<QuerySnapshot> {
        return CompletedProjectCollection.get()

    }
    fun deleteproject(completedprojectModal: CompletedprojectModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        CompletedProjectCollection.document(completedprojectModal.docId).delete()
            .addOnSuccessListener {
                result.value = true

            }
            .addOnFailureListener {
                result.value = false
            }
        return result
    }

    fun edit_completed_project(completedprojectModal: CompletedprojectModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        jobCollection.document(completedprojectModal.docId).set(completedprojectModal)
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
    fun add_team_member(teamModal: TeamModal):LiveData<Boolean>
    {

        var result = MutableLiveData<Boolean>()

        TeamCollection.add(teamModal).addOnSuccessListener()
        {document->
            teamModal.userId = document.id

                    TeamCollection.document(document.id).set(teamModal)
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
    fun deleteTeamMember(teamModal: TeamModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        TeamCollection.document(teamModal.userId).delete()
            .addOnSuccessListener {
                result.value = true

            }
            .addOnFailureListener {
                result.value = false
            }
        return result
    }
    fun get_team_memeber_list():Task<QuerySnapshot>
    {
        return TeamCollection.get()
    }
    fun edit_Team_member(teamModal: TeamModal): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        TeamCollection.document(teamModal.userId).set(teamModal)
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

    fun delte_user(usermodel: Usermodel): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        UserCollection.document(usermodel.userId).delete()
            .addOnSuccessListener {
                result.value = true

            }
            .addOnFailureListener {
                result.value = false
            }
        return result
    }
    fun edit_user(usermodel: Usermodel):LiveData<Boolean>
    {
        val result = MutableLiveData<Boolean>()
        UserCollection.document(usermodel.userId).set(usermodel)
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


}