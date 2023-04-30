package com.amorphteam.ketub.ui.epub.fragments.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.ui.epub.fragments.toc.TocSingleViewModel

class SearchSingleFragment : Fragment() {

    private lateinit var binding: com.amorphteam.ketub.databinding.FragmentSearchSingleBinding
    private lateinit var viewModel: SearchSingleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SearchSingleViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search_single, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


}