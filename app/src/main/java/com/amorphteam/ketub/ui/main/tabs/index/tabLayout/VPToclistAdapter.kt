package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class VPToclistAdapter(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()

    public fun getTabTitle(position: Int): String {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}


