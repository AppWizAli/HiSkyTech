package com.hiskytech.portfolio.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.Data.Repo
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
}