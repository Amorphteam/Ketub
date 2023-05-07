package com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentTocBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.adapter.IndexExpandableAdapter
import com.amorphteam.ketub.model.TocGroupItem


class TocFragment(val catName:String) : Fragment() {
    private lateinit var binding: FragmentTocBinding
    private lateinit var viewModel: TocViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_toc, container, false
        )
        val viewModelFactory = TocViewModelFactory(catName)
        viewModel = ViewModelProvider(this, viewModelFactory)[TocViewModel::class.java]


        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }



        viewModel.tocGroupItems.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                handleTocRecyclerView(it)
            }
        }

        return binding.root
    }

    private fun handleTocRecyclerView(it: List<TocGroupItem>) {
        val adapter = IndexExpandableAdapter()
        adapter.submitList(it)
        binding.expandableListView.setAdapter(adapter)

        adapter.clickListener.setOnGroupClickListener {
            viewModel.openEpubAct()
        }
        handleSearchView(adapter)
    }

    private fun handleSearchView(
        index: IndexExpandableAdapter
    ) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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

    private fun filterSearch(searchString: String, index: IndexExpandableAdapter) {
        index.filter.filter(searchString)
    }

}