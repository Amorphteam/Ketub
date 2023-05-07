package com.amorphteam.nososejtehad.bookmark

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentBookmarkContainerBinding
import com.amorphteam.ketub.ui.adapter.ViewPagerBookmarkListAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkFragment
import com.google.android.material.tabs.TabLayoutMediator


class BookmarkContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[BookmarkContainerViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentBookmarkContainerBinding>(
            inflater,
            R.layout.fragment_bookmark_container,
            container,
            false
        )
        handleTabViewPager(binding)

        return binding.root
    }

    private fun handleTabViewPager(binding: FragmentBookmarkContainerBinding) {
        val adapter = ViewPagerBookmarkListAdapter(activity)
        val firsBookmarkFragment = BookmarkFragment(getString(R.string.nosos_title))
        val secondBookmarkFragment = BookmarkFragment(getString(R.string.ejtehad_title))
        adapter.addFragment(firsBookmarkFragment, getString(R.string.nosos_title))
        adapter.addFragment(secondBookmarkFragment, getString(R.string.ejtehad_title))

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 1
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }


}