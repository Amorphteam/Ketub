package com.amorphteam.ketub.ui.main.tabs.toc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
    var firstTocFragment: TocFragment? = null
    var secondTocFragment: TocFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (firstTocFragment == null) {
            Log.i(Keys.LOG_NAME, "first is null")
            firstTocFragment = TocFragment(Keys.DB_FIRST_CAT)
        }
        if (secondTocFragment == null) {
            Log.i(Keys.LOG_NAME, "second is null")
            secondTocFragment = TocFragment(Keys.DB_SECOND_CAT)
        }



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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(Keys.LOG_NAME, "onDestrou")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(Keys.LOG_NAME, "onDestffffrou")

    }
    private fun handleTabViewPager(binding: FragmentTocContainerBinding) {
        val adapter = ViewPagerAdapter(activity)
        firstTocFragment?.let { adapter.addFragment(it, Keys.DB_FIRST_CAT) }
        secondTocFragment?.let { adapter.addFragment(it, Keys.DB_SECOND_CAT) }

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}






