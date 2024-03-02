package com.hiskytech.portfolio.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiskytech.portfolio.Adapters.AdapterAdmin
import com.hiskytech.portfolio.Models.ModelUser
import com.hiskytech.portfolio.databinding.FragmentUserFragmentsBinding


class UserFragments : Fragment() ,AdapterAdmin.OnItemClickListener{
    private lateinit var binding: FragmentUserFragmentsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFragmentsBinding.inflate(inflater,container,false)
//        setAdapter()
        return binding.root
    }

//    private fun setAdapter() {
//        val userList : ArrayList<ModelUser> = arrayListOf()
//        viewModal.getUserList().addOnCompleteListener() { user->
//            if (user.isSuccessful)
//            {
//                if (user.result.size()>0)
//                {
//                    for (document in user.result)
//                    {
//                        userList .add(document.toObject(ModelUser::class.java))
//                    }
//
//                }
//                userList.sortBy { it.name }
//                binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
//                binding.recyclerview.adapter = AdapterAdmin(requireContext(),userList,this@UserFragments)
//
//            }
//        }
//    }

    override fun onUpdateButton(modelUser: ModelUser) {

    }

    override fun onRemoveButton(modelUser: ModelUser) {

    }

    override fun onViewButton(modelUser: ModelUser) {

    }
}