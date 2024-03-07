package com.hiskytech.portfolio.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.Data.Repo
import com.hiskytech.portfolio.Models.Usermodel

class UserViewModal : ViewModel() {
    var repo = Repo(this@UserViewModal)

   fun adduser(usermodel: Usermodel):LiveData<Boolean>
   {
       return repo.adduser(usermodel)
   }

    fun get_data(): Task<QuerySnapshot>{
        return repo.get_data()

    }


}