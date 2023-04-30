package com.amorphteam.ketub.ui.epub.fragments.toc

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
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.epub.fragments.toc.adapter.TocSingleExpandableAdapter
import com.amorphteam.ketub.ui.main.tabs.index.adapter.IndexExpandableAdapter

class TocSingleFragment : Fragment() {
    private lateinit var binding: com.amorphteam.ketub.databinding.FragmentTocSingleBinding
    private lateinit var viewModel: TocSingleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TocSingleViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_toc_single, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubFrag.observe(viewLifecycleOwner) {
//            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }
        val adapter = TocSingleExpandableAdapter()
        adapter.submitList(viewModel.indexGroupItems.value!!)
        binding.expandableListView.setAdapter(adapter)

        adapter.clickListener.setOnGroupClickListener {
            viewModel.openEpubFrag()
        }

        handleSearchView(binding.searchView, adapter)

        return binding.root
    }

    private fun handleSearchView(
        searchView: androidx.appcompat.widget.SearchView,
        index: TocSingleExpandableAdapter
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

    private fun filterSearch(searchString: String, index: TocSingleExpandableAdapter) {
        index.filter.filter(searchString)
    }


}