package com.amorphteam.ketub.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.amorphteam.ketub.ui.epub.fragments.epubViewer.EpubViewerFragment
import java.lang.ref.WeakReference

class EpubVerticalAdapter(val mSpine: ArrayList<String>, fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var mCurrentFragment: WeakReference<EpubViewerFragment>? = null
    fun getCurrentFragment(): EpubViewerFragment? {
        return mCurrentFragment?.get()
    }



    override fun getItemCount(): Int {
        return mSpine.size
    }

    override fun createFragment(p0: Int): Fragment {

        //TODO// HANDLE EPUBVERTICALDELEGATE
//        EpubVerticalDelegate.get().getActivity().addStyleListeners(fragment)
        return EpubViewerFragment()
    }

    //TODO:// HANDLE NAVPOINT
//    fun setCurrentPage(index: Int, navPoint: NavPoint?) {
//        if (navPoint != null) {
//            mCurrentFragment!!.get().navigateToHeader(navPoint.hashNavigation)
//        }
//    }
}