package com.amorphteam.ketub.ui.epub

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubViewerBinding
import kotlinx.android.synthetic.main.activity_epub_viewer.*


class EpubViewer : AppCompatActivity() {

    private lateinit var viewModel: EpubViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val epubBinding: ActivityEpubViewerBinding = DataBindingUtil.setContentView(this, R.layout.activity_epub_viewer)
        viewModel = ViewModelProvider(this).get(EpubViewerViewModel::class.java)
        epubBinding.epubViewerViewModel = viewModel
        epubBinding.lifecycleOwner = this

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        //TODO: NEED TO MOVE IT TO VIEWMODEL
        seekBar.hintDelegate
            .setHintAdapter { _, progress -> "Progress: $progress" }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_epub, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.search -> {
            true
        }

        R.id.setting -> {

            true
        }

        R.id.bookmark -> {

            true
        }

        R.id.toc -> {

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



}