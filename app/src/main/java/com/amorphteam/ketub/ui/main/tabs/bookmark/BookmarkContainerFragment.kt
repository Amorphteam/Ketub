package com.amorphteam.ketub.ui.main.tabs.bookmark

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentBookmarkContainerBinding
import com.amorphteam.ketub.ui.adapter.ViewPagerAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkFragment
import com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second.TocFragment
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.nososejtehad.bookmark.BookmarkContainerViewModel
import com.google.android.material.tabs.TabLayoutMediator


class BookmarkContainerFragment : Fragment() {
    private var firsBookmarkFragment:BookmarkFragment? = null
    private var secondBookmarkFragment:BookmarkFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[BookmarkContainerViewModel::class.java]

        firsBookmarkFragment = BookmarkFragment(Keys.DB_FIRST_CAT)
        secondBookmarkFragment = BookmarkFragment(Keys.DB_SECOND_CAT)
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
        val adapter = ViewPagerAdapter(activity)
        firsBookmarkFragment?.let { adapter.addFragment(it, Keys.DB_FIRST_CAT) }
        secondBookmarkFragment?.let { adapter.addFragment(it, Keys.DB_SECOND_CAT) }

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 1
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }


}