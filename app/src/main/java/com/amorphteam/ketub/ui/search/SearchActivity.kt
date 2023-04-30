package com.amorphteam.ketub.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivitySearchBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.search.adapter.SearchClickListener
import com.amorphteam.ketub.ui.search.adapter.SearchListAdapter


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
        setupChipGroup()
    }

    private fun setupChipGroup() {
        binding.chip1.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
            Toast
                .makeText(this, "Removed 1st Chip", Toast.LENGTH_SHORT)
                .show()
        }

        binding.chip2.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
            Toast
                .makeText(this, "Removed 2nd Chip", Toast.LENGTH_SHORT)
                .show()
        }
        binding.chip3.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
            Toast
                .makeText(this, "Removed 3rd Chip", Toast.LENGTH_SHORT)
                .show()
        }

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