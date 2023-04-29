package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkDeleteClickListener
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkListAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabase
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.Keys

class BookmarkListFirstFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkListFirstBinding
    private lateinit var viewModel: BookmarkListFirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bookmark_list_first, container, false
        )

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = BookmarkDatabase.getInstance(application).bookmarkDatabaseDao
        val viewModelFactory = BookmarkListFirstViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel =
            ViewModelProvider(this, viewModelFactory)[BookmarkListFirstViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        }

        val adapter = BookmarkListAdapter(BookmarkClickListener {
            viewModel.openEpubAct()

        }, BookmarkDeleteClickListener {
            Log.i(Keys.LOG_NAME, "BookmarkDeleteClickListener$it")
            viewModel.deleteBookmark(it)

        })

        viewModel.allBookmarks.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                handleBookmarkRecyclerView(adapter, it)
            }
        }

        handleSearchView(binding.searchView, adapter)

        return binding.root

    }

    private fun handleBookmarkRecyclerView(
        index: BookmarkListAdapter,
        bookmarkArrayList: List<BookmarkModel>
    ) {
        index.submitList(bookmarkArrayList)
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