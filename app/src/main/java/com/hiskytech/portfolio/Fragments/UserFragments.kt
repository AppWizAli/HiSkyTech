package com.hiskytech.portfolio.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hiskytech.portfolio.Adapters.AdapterAdmin
import com.hiskytech.portfolio.Models.ModelUser
import com.hiskytech.portfolio.Models.Usermodel
import com.hiskytech.portfolio.ViewModels.UserViewModal
import com.hiskytech.portfolio.databinding.FragmentUserFragmentsBinding


class UserFragments : Fragment() ,AdapterAdmin.OnItemClickListener{
    private lateinit var binding: FragmentUserFragmentsBinding
    private  val userViewModal: UserViewModal by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFragmentsBinding.inflate(inflater,container,false)

        return binding.root
    }




    override fun onUpdateButton(usermodel: Usermodel) {
        TODO("Not yet implemented")
    }

    override fun onRemoveButton(usermodel: Usermodel) {
        TODO("Not yet implemented")
    }

    override fun onViewButton(usermodel: Usermodel) {
        TODO("Not yet implemented")
    }
}