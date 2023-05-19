package com.amorphteam.ketub.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val bookRepository = BookRepository(bookDao)
        val viewModelFactory = SearchViewModelFactory(bookRepository)

        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        viewModel.allBooks.observe(this) {
            val booksStringAddress = ArrayList<String>()
            for (item in it){
                Log.i(Keys.LOG_NAME, item.bookPath.toString())
               val bookAddress = EpubHelper.getBookAddressFromBookPath(item.bookPath!!, this)
                bookAddress?.let { it1 -> booksStringAddress.add(it1) }
            }
            val searchHelper = SearchHelper(this)
            viewModel.searchAllBooks(searchHelper, booksStringAddress, "نواياه")
        }

        viewModel.results.observe(this){
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

    private fun setupChipGroup(adapter: SearchListAdapter) {
        binding.chip1.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
        }
        binding.chip1.setOnClickListener {
            Log.i(Keys.LOG_NAME, "chip1")
            adapter.filter.filter("")

        }

        binding.chip2.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
            Log.i(Keys.LOG_NAME, "chip2")

        }
        binding.chip2.setOnClickListener {
            adapter.filter.filter("الاجتهاد والتجديد")
            Log.i(Keys.LOG_NAME, "chip1")

        }
        binding.chip3.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
            Log.i(Keys.LOG_NAME, "chip3")

        }
        binding.chip3.setOnClickListener {
            adapter.filter.filter("نصوص معاصرة")
            Log.i(Keys.LOG_NAME, "chip1")
        }

    }

    private fun handleSearchResult(arrayResult: List<SearchInfoHolder>) {
        val adapter = SearchListAdapter(SearchClickListener { id ->
            viewModel.openEpubAct()
        })
        adapter.submitList(arrayResult)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        setupChipGroup(adapter)

    }

}