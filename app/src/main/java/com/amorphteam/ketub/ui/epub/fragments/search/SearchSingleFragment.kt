package com.amorphteam.ketub.ui.epub.fragments.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.ui.adapter.SearchClickListener
import com.amorphteam.ketub.ui.adapter.SearchListAdapter

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
        viewModel.startEpubFrag.observe(requireActivity()) {
//            if (it) startActivity(Intent(this, EpubActivity::class.java))
        }
        val adapter = SearchListAdapter(SearchClickListener { id ->
            viewModel.openEpubFrag()
        })
        binding.searchbar.back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
        }
        handleSearchResult(adapter)
        return binding.root
    }


    private fun handleSearchResult(adapter: SearchListAdapter) {
//        adapter.submitList(viewModel.getSearchList().value)
//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = adapter
    }



}