package com.amorphteam.ketub.ui.epub.fragments.epubViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentEpubContainerBinding
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.utility.Keys
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem

class EpubContainerFragment : Fragment() {
    private lateinit var binding: FragmentEpubContainerBinding
    private lateinit var viewModel: EpubContainerViewModel
    private var spineItems: ArrayList<ManifestItem>? = null
    private var navPoint: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_epub_container, container, false
        )
        viewModel = ViewModelProvider(this)[EpubContainerViewModel::class.java]

        return binding.root
    }

    private fun handleViewEpubPager(spineItems: ArrayList<ManifestItem>) {
        val adapter =
            EpubVerticalAdapter(spineItems, requireActivity().supportFragmentManager, lifecycle)
        binding.epubVerticalViewPager.adapter = adapter
        binding.epubVerticalViewPager.offscreenPageLimit = Keys.MAX_SIDE_PAGE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            spineItems = requireArguments().getParcelableArrayList(Keys.SPINE)
            navPoint = requireArguments().getInt(Keys.NAV_POINT)
            spineItems?.let { handleViewEpubPager(it) }
        }
    }


    companion object {
        fun newInstance(
            spines: ArrayList<ManifestItem>,
            navPoint: Int
        ): EpubContainerFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList(Keys.SPINE, spines)
            bundle.putInt(Keys.NAV_POINT, navPoint)
            val instance: EpubContainerFragment =
                EpubContainerFragment()
            instance.arguments = bundle
            return instance
        }

    }


}