package com.amorphteam.ketub.ui.main.tabs.bookmark.tabLayout

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
import com.amorphteam.ketub.databinding.FragmentBookmarkListFirstBinding
import com.amorphteam.ketub.ui.epub.EpubViewer
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkClickListener
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkListAdapter

class BookmarkListFirstFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkListFirstBinding
    private lateinit var viewModel: BookmarkListFirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[BookmarkListFirstViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bookmark_list_first, container, false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        }

        val adapter = BookmarkListAdapter(BookmarkClickListener {
            viewModel.openEpubAct()
        })

        handleBookmarkRecyclerView(adapter)

        handleSearchView(binding.searchView, adapter)

        return binding.root

    }

    private fun handleBookmarkRecyclerView(index: BookmarkListAdapter) {
        index.submitList(viewModel.getIndexList().value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = index
    }

    private fun handleSearchView(
        searchView: androidx.appcompat.widget.SearchView,
        index: BookmarkListAdapter
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

    private fun filterSearch(searchString: String, index: BookmarkListAdapter) {
        index.filter.filter(searchString)
    }

}