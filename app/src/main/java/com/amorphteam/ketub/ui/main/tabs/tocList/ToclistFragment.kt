package com.amorphteam.ketub.ui.main.tabs.tocList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentToclistBinding
import com.amorphteam.ketub.ui.main.tabs.library.tabLayout.BookmarksFragment
import com.amorphteam.ketub.ui.main.tabs.tocList.tabLayout.TitleFragment
import com.amorphteam.ketub.ui.main.tabs.tocList.tabLayout.VPToclistAdapter
import com.google.android.material.tabs.TabLayout

class ToclistFragment : Fragment() {
    private lateinit var viewModel: ToclistFragmentViewModel
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding: FragmentToclistBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_toclist, container, false)
        viewModel = ViewModelProvider(this).get(ToclistFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabLayout = view.findViewById(R.id.tablayout)
        viewPager = view.findViewById(R.id.viewpager)

        tabLayout!!.setupWithViewPager(viewPager)
        val adapter: VPToclistAdapter = VPToclistAdapter(requireActivity().supportFragmentManager)
        adapter.addFragment(TitleFragment(), getString(R.string.titles))
        adapter.addFragment(BookmarksFragment(), getString(R.string.bookmarks))

        viewPager!!.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        tabLayout = null
        viewPager!!.adapter = null
        viewPager = null
    }


}