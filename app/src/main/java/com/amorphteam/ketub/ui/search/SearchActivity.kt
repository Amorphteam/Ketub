package com.amorphteam.ketub.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookDatabase
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.databinding.ActivitySearchBinding
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.ui.adapter.SearchClickListener
import com.amorphteam.ketub.ui.adapter.SearchListAdapter
import com.amorphteam.ketub.utility.EpubHelper
import com.amorphteam.ketub.utility.Keys


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



    }


    fun startSearch(query:String){
        if (searchHelper != null){
            searchHelper?.stopSearch(true)
            viewModel.clearList()
            searchHelper= null
        }
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
                binding.chipGroup.visibility = View.GONE
//                if (newText.length > 2) {
//                    startSearch(newText)
//                }
                return true
            }
        })

    }
    private fun setupChipGroup(adapter: SearchListAdapter) {

        binding.chip1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
            searchHelper?.stopSearch(true)
            adapter.filter.filter(" ")
        }
        }

        binding.chip2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchHelper?.stopSearch(true)
                adapter.filter.filter(Keys.DB_FIRST_CAT.split(" ").first())
            }
        }

        binding.chip3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                searchHelper?.stopSearch(true)
                adapter.filter.filter(Keys.DB_SECOND_CAT.split(" ").first())
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        searchHelper?.stopSearch(true)
        searchHelper = null
    }

    private fun handleSearchResult(arrayResult: List<SearchModel>) {
        val adapter = SearchListAdapter(SearchClickListener {
            it.bookAddress?.let { it1 -> it.pageId?.let { it2 ->
                EpubHelper.openEpub(it1,
                    it2, this)
            } }
        })
        adapter.submitList(arrayResult)
        binding.chipGroup.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        setupChipGroup(adapter)

    }

}