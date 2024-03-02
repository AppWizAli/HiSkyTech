package com.hiskytech.portfolio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hiskytech.portfolio.Fragments.HomeFragments
import com.hiskytech.portfolio.Fragments.UserFragments

class ViewpagerAdapter (frag: FragmentActivity, private var totalCount: Int) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->HomeFragments()
            1 -> UserFragments()

            else -> HomeFragments()

        }
    }
}