package com.amorphteam.ketub.ui.main.tabs.toc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentTocBinding
import com.amorphteam.ketub.ui.main.tabs.toc.adapter.ViewPagerIndexListAdapter
import com.amorphteam.ketub.ui.main.tabs.toc.tabs.first.TocFirstFragment
import com.amorphteam.ketub.ui.main.tabs.toc.tabs.second.TocSecondFragment
import com.amorphteam.nososejtehad.index.TocViewModel
import com.google.android.material.tabs.TabLayoutMediator


class TocFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProvider(this)[TocViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentTocBinding>(
            inflater,
            R.layout.fragment_toc,
            container,
            false
        )

        handleTabViewPager(binding)

        return binding.root
    }

    private fun handleTabViewPager(binding: FragmentTocBinding) {
        val adapter = ViewPagerIndexListAdapter(activity)
        adapter.addFragment(TocFirstFragment(), getString(R.string.nosos_title))
        adapter.addFragment(TocSecondFragment(), getString(R.string.ejtehad_title))

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 1
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}






