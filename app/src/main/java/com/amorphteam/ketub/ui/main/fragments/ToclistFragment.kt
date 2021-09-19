package com.amorphteam.ketub.ui.main.fragments

import android.net.VpnService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubViewerBinding
import com.amorphteam.ketub.databinding.FragmentToclistBinding
import com.amorphteam.ketub.ui.epub.EpubViewerViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_toclist.*

class ToclistFragment : Fragment() {
    private lateinit var viewModel: ToclistFragmentViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding: FragmentToclistBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_toclist, container, false)
        viewModel = ViewModelProvider(this).get(ToclistFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initTabLayout()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initTabLayout() {
        tabLayout = requireActivity().findViewById(R.id.tablayout)
        viewPager = requireActivity().findViewById(R.id.viewpager)

        tabLayout.setupWithViewPager(viewPager)
        val adapter: VPToclistAdapter = VPToclistAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(TitleFragment(), getString(R.string.titles))
        adapter.addFragment(BookmarksFragment(), getString(R.string.bookmarks))

        viewPager.adapter = adapter
    }


}