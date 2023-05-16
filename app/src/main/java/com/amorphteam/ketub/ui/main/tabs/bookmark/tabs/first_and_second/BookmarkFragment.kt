package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

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
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.databinding.FragmentBookmarkBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.adapter.ItemClickListener
import com.amorphteam.ketub.ui.adapter.DeleteClickListener
import com.amorphteam.ketub.ui.adapter.ReferenceAdapter
import com.amorphteam.ketub.database.reference.ReferenceDatabase
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.ui.main.tabs.library.LibraryViewModelFactory
import com.amorphteam.ketub.utility.EpubHelper

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
        val referenceDao = ReferenceDatabase.getInstance(application).referenceDatabaseDao
        val referenceRepository = ReferenceRepository(referenceDao)
        val viewModelFactory = BookmarkViewModelFactory(referenceRepository, catName)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[BookmarkViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.allBookmarks.observe(viewLifecycleOwner) {
                handleBookmarkRecyclerView(it)
        }
        return binding.root
    }

    private fun handleBookmarkRecyclerView(
        bookmarkArrayList: List<ReferenceModel>
    ) {
        val adapter = ReferenceAdapter(ItemClickListener {
            val bookPath = it.bookPath
            val bookAddress = EpubHelper.getBookAddressFromBookPath(bookPath, requireContext())
            it.navIndex?.let { it1 ->
                if (bookAddress != null) {
                    EpubHelper.openEpub(bookAddress, it1, requireContext())
                }
            }

        }, DeleteClickListener {
            viewModel.deleteBookmark(it)
        })
        adapter.submitList(bookmarkArrayList)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        handleSearchView(adapter)
    }

    private fun handleSearchView(
        index: ReferenceAdapter
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

    private fun filterSearch(searchString: String, index: ReferenceAdapter) {
        index.filter.filter(searchString)
    }

}