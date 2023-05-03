package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

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
import com.amorphteam.ketub.databinding.FragmentBookmarkBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkClickListener
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkDeleteClickListener
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkListAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.database.BookmarkDatabase
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.Keys

class BookmarkFragment(val catName:String) : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bookmark, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = BookmarkDatabase.getInstance(application).bookmarkDatabaseDao
        val viewModelFactory = BookmarkViewModelFactory(dataSource, catName)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[BookmarkViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }

        viewModel.allBookmarks.observe(viewLifecycleOwner) {
                handleBookmarkRecyclerView(it)
        }
        return binding.root
    }

    private fun handleBookmarkRecyclerView(
        bookmarkArrayList: List<BookmarkModel>
    ) {
        val adapter = BookmarkListAdapter(BookmarkClickListener {
            viewModel.openEpubAct()

        }, BookmarkDeleteClickListener {
            viewModel.deleteBookmark(it)
        })
        adapter.submitList(bookmarkArrayList)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        handleSearchView(adapter)
    }

    private fun handleSearchView(
        index: BookmarkListAdapter
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

    private fun filterSearch(searchString: String, index: BookmarkListAdapter) {
        index.filter.filter(searchString)
    }

}