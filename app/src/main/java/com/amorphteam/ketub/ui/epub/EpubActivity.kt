package com.amorphteam.ketub.ui.epub

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubBinding
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.ui.epub.fragments.search.SearchSingleFragment
import com.amorphteam.ketub.utility.Keys
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem


class EpubActivity : AppCompatActivity() {
    lateinit var binding: ActivityEpubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding =
            DataBindingUtil.setContentView(this, R.layout.activity_epub)
        val viewModel = ViewModelProvider(this).get(EpubViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)

        viewModel.spineArray.observe(this){
            handleViewEpubPager(it)

        }
        if (intent.extras != null) {
            val bookAddress = intent.getStringExtra(Keys.BOOK_ADDRESS)
            val navPoint = intent.getIntExtra(Keys.NAV_POINT, 0)
            viewModel.getBookAddress(bookAddress)

        }

    }


    private fun handleViewEpubPager(spineItems: ArrayList<ManifestItem>) {
        val adapter =
            EpubVerticalAdapter(spineItems, this.supportFragmentManager, lifecycle)
        binding.epubVerticalViewPager.adapter = adapter
        binding.epubVerticalViewPager.offscreenPageLimit = Keys.MAX_SIDE_PAGE
    }

    private fun handleFragment(newFragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        binding.epubVerticalViewPager.visibility = View.GONE
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_epub, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
//                openStyleSheet()
                true
            }
            R.id.home -> {
                onBackPressed()
                true
            }

            R.id.toc -> {
//                openTocFragment()
                true
            }

            R.id.bookmark -> {
//                openBookmarkFragment()
                true
            }
            R.id.search -> {
                handleFragment(SearchSingleFragment())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}