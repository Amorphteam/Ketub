package com.amorphteam.ketub.ui.epub.fragments.search

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.epub.fragments.toc.TocSingleViewModel
import com.amorphteam.ketub.ui.search.adapter.SearchClickListener
import com.amorphteam.ketub.ui.search.adapter.SearchListAdapter

class SearchSingleFragment : Fragment() {

    private lateinit var binding: com.amorphteam.ketub.databinding.FragmentSearchSingleBinding
    private lateinit var viewModel: SearchSingleViewModel
    lateinit var navController: NavController

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
        viewModel.startEpubFrag.observe(requireActivity()) {
//            if (it) startActivity(Intent(this, EpubActivity::class.java))
        }
        val adapter = SearchListAdapter(SearchClickListener { id ->
            viewModel.openEpubFrag()
        })

        handleSearchResult(adapter)

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Search"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            openEpubFragment()
        }

        return binding.root
    }

    private fun openEpubFragment() {
        navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_searchSingleFragment_to_epubViewFragment)
    }
    private fun handleSearchResult(adapter: SearchListAdapter) {
        adapter.submitList(viewModel.getSearchList().value)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }



}