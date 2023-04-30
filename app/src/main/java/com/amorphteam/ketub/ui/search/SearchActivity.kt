package com.amorphteam.ketub.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivitySearchBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.main.tabs.bookmark.adapter.BookmarkListAdapter
import com.amorphteam.ketub.ui.search.adapter.SearchClickListener
import com.amorphteam.ketub.ui.search.adapter.SearchListAdapter
import com.amorphteam.ketub.utility.Keys


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
        val adapter = SearchListAdapter(SearchClickListener { id ->
            viewModel.openEpubAct()
        })

        setupChipGroup(adapter)
        handleSearchResult(adapter)
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

private fun handleSearchResult(adapter: SearchListAdapter) {
        adapter.submitList(viewModel.getSearchList().value)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }
}