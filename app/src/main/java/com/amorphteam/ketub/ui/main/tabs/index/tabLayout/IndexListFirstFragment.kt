package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentIndexListFirstBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.main.tabs.index.adapter.IndexClickListener
import com.amorphteam.ketub.ui.main.tabs.index.adapter.IndexListAdapter

class IndexListFirstFragment : Fragment() {

    private lateinit var binding: FragmentIndexListFirstBinding
    private lateinit var viewModel: IndexListFirstViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[IndexListFirstViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_index_list_first, container, false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }

        val adapter = IndexListAdapter(IndexClickListener {
            viewModel.openEpubAct()
        })

        handleIndexRecyclerView(adapter)

        handleSearchView(binding.searchView , adapter)

        return binding.root

    }


    private fun handleIndexRecyclerView(index: IndexListAdapter) {
        index.submitList(viewModel.getIndexList().value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = index
    }

    private fun handleSearchView(searchView: androidx.appcompat.widget.SearchView, index: IndexListAdapter) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterSearch(newText, index)

                return true
            }
        })

    }

    private fun filterSearch(searchString: String, index: IndexListAdapter) {
        index.filter.filter(searchString)
    }

}