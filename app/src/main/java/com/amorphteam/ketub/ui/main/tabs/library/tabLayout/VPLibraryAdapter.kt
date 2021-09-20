package com.amorphteam.ketub.ui.main.tabs.library.tabLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VPLibraryAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragmentArrayList: ArrayList<Fragment> = ArrayList()
    private var fragmentTitle: ArrayList<String> = ArrayList()
    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    fun addFragment(fargment: Fragment, title:String){
        fragmentArrayList.add(fargment)
        fragmentTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitle[position]
    }
}
