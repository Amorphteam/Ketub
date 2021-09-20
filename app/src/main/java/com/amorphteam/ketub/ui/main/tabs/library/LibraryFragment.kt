package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentLibraryBinding
import com.amorphteam.ketub.ui.epub.EpubViewer
import com.amorphteam.ketub.ui.main.tabs.library.tabLayout.AllTabFragment
import com.amorphteam.ketub.ui.main.tabs.library.tabLayout.AuthorTabFragment
import com.amorphteam.ketub.ui.main.tabs.tocList.tabLayout.CatTabFragment
import com.amorphteam.ketub.ui.main.tabs.library.tabLayout.VPLibraryAdapter


class LibraryFragment : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager:ViewPager? = null
    private lateinit var viewModel: LibraryFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentLibraryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_library, container, false
        )

        viewModel = ViewModelProvider(this).get(LibraryFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner, {
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        })
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            tabLayout = view.findViewById(R.id.tablayout)
            viewPager = view.findViewById(R.id.viewpager)
            tabLayout!!.setupWithViewPager(viewPager)
            val vpAdapter = VPLibraryAdapter(requireActivity().supportFragmentManager)
            vpAdapter.addFragment(AllTabFragment(), getString(R.string.all_tab))
            vpAdapter.addFragment(AuthorTabFragment(), getString(R.string.author_tab))
            vpAdapter.addFragment(CatTabFragment(), getString(R.string.cat_tab))
            viewPager!!.adapter = vpAdapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        tabLayout = null
        viewPager!!.adapter = null
        viewPager = null
    }
}