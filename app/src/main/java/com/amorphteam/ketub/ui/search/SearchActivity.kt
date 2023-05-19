package com.amorphteam.ketub.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookDatabase
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.database.reference.ReferenceDatabase
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.databinding.ActivitySearchBinding
import com.amorphteam.ketub.model.SearchInfoHolder
import com.amorphteam.ketub.ui.adapter.IndexExpandableAdapter
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.epub.EpubViewModelFactory
import com.amorphteam.ketub.ui.main.tabs.library.LibraryViewModel
import com.amorphteam.ketub.ui.main.tabs.library.LibraryViewModelFactory
import com.amorphteam.ketub.ui.search.adapter.SearchClickListener
import com.amorphteam.ketub.ui.search.adapter.SearchListAdapter
import com.amorphteam.ketub.utility.EpubHelper
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.Keys.Companion.ARG_SEARCH_WORD
import com.amorphteam.ketub.utility.Keys.Companion.BOOKS
import kotlinx.android.synthetic.main.item_group_index.*


class SearchActivity : AppCompatActivity() {
    var searchHelper: SearchHelper? = null
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private var allBooksString = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val bookRepository = BookRepository(bookDao)
        val viewModelFactory = SearchViewModelFactory(bookRepository)

        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        viewModel.allBooks.observe(this) {
            for (item in it){
                Log.i(Keys.LOG_NAME, item.bookPath.toString())
               val bookAddress = EpubHelper.getBookAddressFromBookPath(item.bookPath!!, this)
                bookAddress?.let { it1 -> allBooksString.add(it1) }
            }
            handleSearchView()
        }

        viewModel.results.observe(this){
            Log.i(Keys.LOG_NAME, it.size.toString())
            handleSearchResult(it)
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.searchbar.back.setOnClickListener {
            onBackPressed()
        }
        viewModel.startEpubAct.observe(this) {
            if (it) startActivity(Intent(this, EpubActivity::class.java))
        }


    }

    fun startSearch(query:String){
        searchHelper = SearchHelper(this)
        viewModel.searchAllBooks(searchHelper!!, allBooksString, query)
    }


    private fun handleSearchView() {
        binding.searchbar.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                startSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                if (newText.length > 2) {
//                    startSearch(newText)
//                }
                return true
            }
        })

    }
    private fun setupChipGroup(adapter: SearchListAdapter) {

        binding.chip1.setOnClickListener {
            searchHelper?.stopSearch(true)
            adapter.filter.filter(" ")
        }

        binding.chip2.setOnClickListener {
            searchHelper?.stopSearch(true)
            adapter.filter.filter(Keys.DB_FIRST_CAT)
        }

        binding.chip3.setOnClickListener {
            searchHelper?.stopSearch(true)
            adapter.filter.filter(Keys.DB_SECOND_CAT)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        searchHelper?.stopSearch(true)
        searchHelper = null
    }

    private fun handleSearchResult(arrayResult: List<SearchInfoHolder>) {
        val adapter = SearchListAdapter(SearchClickListener { id ->
            searchHelper?.stopSearch(true)
            viewModel.openEpubAct()
        })
        adapter.submitList(arrayResult)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        setupChipGroup(adapter)

    }

}