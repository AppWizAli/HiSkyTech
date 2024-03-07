package com.hiskytech.portfolio.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.ViewModels.UserViewModal
import com.hiskytech.portfolio.databinding.FragmentUserFragmentsBinding


class UserFragments : Fragment(){
    private lateinit var binding: FragmentUserFragmentsBinding
    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFragmentsBinding.inflate(inflater, container, false)

        bottomNavigationView = binding.navView
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

// Connect BottomNavigationView with NavController
        bottomNavigationView.setupWithNavController(navController)


        return binding.root
    }
}