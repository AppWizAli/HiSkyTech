package com.hiskytech.portfolio.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.hiskytech.portfolio.Data.Repo
import com.hiskytech.portfolio.Models.TeamModal
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
    fun add_team_member(teamModal: TeamModal):LiveData<Boolean> {
        return repo.add_team_member(teamModal)
    }
    fun deleteTeamMember(teamModal: TeamModal): LiveData<Boolean> {
        return repo.deleteTeamMember(teamModal)
    }
    fun get_team_memeber_list():Task<QuerySnapshot>
    {
        return repo.get_team_memeber_list()
    }
    fun edit_Team_member(teamModal: TeamModal): LiveData<Boolean> {
        return repo.edit_Team_member(teamModal)
    }

    fun delte_user(usermodel: Usermodel):LiveData<Boolean>
    {
return repo.delte_user(usermodel)
    }
    fun edit_user(usermodel: Usermodel):LiveData<Boolean>{
        return repo.edit_user(usermodel)
    }

}