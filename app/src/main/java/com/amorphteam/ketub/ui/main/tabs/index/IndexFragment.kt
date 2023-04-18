package com.amorphteam.nososejtehad.index

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentIndexBinding
import com.amorphteam.ketub.ui.main.TabbedActivity
import com.amorphteam.ketub.ui.main.tabs.index.tabLayout.ToclistFirstFragment
import com.amorphteam.ketub.ui.main.tabs.index.tabLayout.ToclistSecondFragment
import com.amorphteam.ketub.ui.main.tabs.index.tabLayout.VPToclistAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.reflect.Array.newInstance
import javax.xml.datatype.DatatypeFactory.newInstance


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

        setAdapterVP(binding)

        return binding.root
    }


    fun setAdapterVP(binding: FragmentIndexBinding) {
        val adapter = VPToclistAdapter(activity)
        adapter.addFragment(ToclistFirstFragment(), getString(R.string.nosos_title))
        adapter.addFragment(ToclistSecondFragment(), getString(R.string.ejtehad_title))

        binding.viewpager.adapter = adapter
        binding.viewpager.currentItem = 0
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}






