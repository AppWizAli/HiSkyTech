package com.hiskytech.portfolio.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.Data.Repo
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CourseModal

class CourseViewModal: ViewModel() {

    var repo = Repo(this@CourseViewModal)
    fun add_course(course_modal: CourseModal): LiveData<Boolean>
    {
        return repo.add_course(course_modal)
    }

    fun getCourseList(): Task<QuerySnapshot> {

            return  repo.getCourseList()
    }

    fun deleteDrama(coursemodal: CourseModal): LiveData<Boolean>
    {
        return  repo.deleteDrama(coursemodal)
    }

    fun updateCourse(courseModal: CourseModal): LiveData<Boolean> {
        return repo.updateCourse(courseModal)
    }

    fun addAnnoucement(annoucementModal: AnnoucementModal): LiveData<Boolean>
    {
        return repo.add_Annoucement(annoucementModal)
    }
}