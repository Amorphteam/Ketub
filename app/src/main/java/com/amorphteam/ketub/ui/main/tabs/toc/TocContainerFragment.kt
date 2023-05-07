package com.amorphteam.ketub.ui.main.tabs.toc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentTocContainerBinding
import com.amorphteam.ketub.ui.adapter.ViewPagerAdapter
import com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second.TocFragment
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.nososejtehad.index.TocContainerViewModel
import com.google.android.material.tabs.TabLayoutMediator


class TocContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProvider(this)[TocContainerViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentTocContainerBinding>(
            inflater,
            R.layout.fragment_toc_container,
            container,
            false
        )

        handleTabViewPager(binding)

        return binding.root
    }

    private fun handleTabViewPager(binding: FragmentTocContainerBinding) {
        val adapter = ViewPagerAdapter(activity)
        val firstTocFragment = TocFragment(Keys.DB_FIRST_CAT)
        val secondTocFragment = TocFragment(Keys.DB_SECOND_CAT)

        adapter.addFragment(firstTocFragment, getString(R.string.nosos_title))
        adapter.addFragment(secondTocFragment, getString(R.string.ejtehad_title))

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 1
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}






