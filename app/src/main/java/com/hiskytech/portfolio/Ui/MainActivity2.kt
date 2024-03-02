package com.hiskytech.portfolio.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.ViewpagerAdapter
import com.hiskytech.portfolio.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set up the ViewPager before setting up the TabLayout
        setupViewPager()

        // Set up the TabLayout after ViewPager
        setupTabLayout()
    }

    private fun setupViewPager() {
        val adapter = ViewpagerAdapter(this@MainActivity2, 2)
        binding.viewPager.adapter = adapter
    }

    private fun setupTabLayout() {
        // Ensure that the ViewPager has an adapter before attaching TabLayoutMediator
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Home"
                1 -> "Users"
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()
    }
}