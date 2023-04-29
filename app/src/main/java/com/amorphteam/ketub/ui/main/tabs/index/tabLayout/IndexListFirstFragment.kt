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
import com.amorphteam.ketub.ui.epub.EpubViewer
import com.amorphteam.ketub.ui.main.tabs.index.adapter.ExpandableAdapter
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
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        }

        val adapter = IndexListAdapter(IndexClickListener {
            viewModel.openEpubAct()
        })

        val adapterExpandable = ExpandableAdapter()
        adapterExpandable.submitList(viewModel.items.value!!)
        binding.expandableListView.setAdapter(adapterExpandable)


        handleSearchView(binding.searchView, adapterExpandable)

        return binding.root
    }


    private fun handleSearchView(
        searchView: androidx.appcompat.widget.SearchView,
        index: ExpandableAdapter
    ) {
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

    private fun filterSearch(searchString: String, index: ExpandableAdapter) {
        index.filter.filter(searchString)
    }

}