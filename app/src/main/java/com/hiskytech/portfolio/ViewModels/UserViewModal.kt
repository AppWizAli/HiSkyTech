package com.hiskytech.portfolio.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.hiskytech.portfolio.Data.Repo

class UserViewModal(context: Application) : AndroidViewModel(context) {

    var repo = Repo(context)

}