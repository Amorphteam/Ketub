package com.amorphteam.ketub.ui.epub.fragments.epubViewer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentEpubContainerBinding
import com.amorphteam.ketub.databinding.FragmentEpubViewBinding
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.utility.Keys
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem

class EpubContainerFragment : Fragment() {
    private lateinit var binding: FragmentEpubContainerBinding
    private lateinit var viewModel: EpubContainerViewModel
    private var spineItem = arrayListOf<String>("ali", "gfjhgjf")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_epub_container, container, false
        )
        viewModel = ViewModelProvider(this)[EpubContainerViewModel::class.java]
        handleViewEpubPager()
        return binding.root
    }

    private fun handleViewEpubPager() {
        val adapter = EpubVerticalAdapter(spineItem, requireActivity().supportFragmentManager, lifecycle)
        binding.epubVerticalViewPager.adapter = adapter
        binding.epubVerticalViewPager.offscreenPageLimit = Keys.MAX_SIDE_PAGE
    }


}