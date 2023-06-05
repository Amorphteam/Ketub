package com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second

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
import com.amorphteam.ketub.ui.adapter.ItemClickListener
import com.amorphteam.ketub.ui.adapter.DeleteClickListener
import com.amorphteam.ketub.ui.adapter.ReferenceAdapter
import com.amorphteam.ketub.database.reference.ReferenceDatabase
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.EpubHelper

class BookmarkFragment(val catName:String = "", val singleBookName:String = "") : Fragment() {
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
        val viewModelFactory = BookmarkViewModelFactory(referenceRepository, catName, singleBookName)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[BookmarkViewModel::class.java]
        if (singleBookName.isEmpty()){
            binding.searchbar.back.visibility = View.GONE
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.searchbar.back.setOnClickListener {
            handleBackPressed()

        }

        viewModel.deleteStatus.observe(viewLifecycleOwner){
            if (it > 0) {
                viewModel.getAllBookmarksFromDB()
            }
        }

        viewModel.allBookmarks.observe(viewLifecycleOwner) {
                handleBookmarkRecyclerView(it)
        }
        return binding.root
    }

    private fun handleBackPressed() {
        requireActivity().onBackPressed()
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
        adapter.setData(bookmarkArrayList)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        handleSearchView(adapter)
    }

    private fun handleSearchView(
        index: ReferenceAdapter
    ) {
        binding.searchbar.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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

    override fun onResume() {
        super.onResume()
        viewModel.getAllBookmarksFromDB()
    }

}