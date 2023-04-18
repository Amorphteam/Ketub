package com.amorphteam.nososejtehad.index

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentIndexBinding
import com.amorphteam.ketub.ui.main.tabs.index.adapter.VPIndexListAdapter
import com.amorphteam.ketub.ui.main.tabs.index.tabLayout.IndexListFirstFragment
import com.amorphteam.ketub.ui.main.tabs.index.tabLayout.IndexListSecondFragment
import com.google.android.material.tabs.TabLayoutMediator


class IndexFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProvider(this)[IndexViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentIndexBinding>(
            inflater,
            R.layout.fragment_index,
            container,
            false
        )

        handleTabViewPager(binding)

        return binding.root
    }

    private fun handleTabViewPager(binding: FragmentIndexBinding) {
        val adapter = VPIndexListAdapter(activity)
        adapter.addFragment(IndexListFirstFragment(), getString(R.string.nosos_title))
        adapter.addFragment(IndexListSecondFragment(), getString(R.string.ejtehad_title))

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}






