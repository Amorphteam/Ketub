package com.amorphteam.ketub.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivitySearchBinding
import com.amorphteam.ketub.databinding.FragmentBookmarkListFirstBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first.BookmarkListFirstViewModel
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookClickListener
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.search.adapter.SearchClickListener
import com.amorphteam.ketub.ui.search.adapter.SearchListAdapter
import com.amorphteam.ketub.ui.search.model.SearchModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(this) {
            if (it) startActivity(Intent(this, EpubActivity::class.java))
        }

        handleSearchResult()

    }

    private fun handleSearchResult() {
        val adapter = SearchListAdapter(SearchClickListener { Id ->
            viewModel.openEpubAct()
        })

        adapter.submitList(viewModel.getSearchList().value)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }
}