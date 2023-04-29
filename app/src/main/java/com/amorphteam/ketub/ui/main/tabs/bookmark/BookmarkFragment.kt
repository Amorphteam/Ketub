package com.amorphteam.nososejtehad.bookmark

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentBookmarkBinding
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.ViewPagerBookmarkListAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first.BookmarkListFirstFragment
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.second.BookmarkListSecondFragment
import com.google.android.material.tabs.TabLayoutMediator


class BookmarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[BookmarkViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentBookmarkBinding>(
            inflater,
            R.layout.fragment_bookmark,
            container,
            false
        )
        handleTabViewPager(binding)

        return binding.root
    }

    private fun handleTabViewPager(binding: FragmentBookmarkBinding) {
        val adapter = ViewPagerBookmarkListAdapter(activity)
        adapter.addFragment(BookmarkListFirstFragment(), getString(R.string.nosos_title))
        adapter.addFragment(BookmarkListSecondFragment(), getString(R.string.ejtehad_title))

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }


}