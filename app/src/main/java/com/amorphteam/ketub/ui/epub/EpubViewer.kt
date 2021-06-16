package com.amorphteam.ketub.ui.epub

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubViewerBinding
import com.amorphteam.ketub.databinding.ActivityMainBinding
import com.amorphteam.ketub.ui.main.MainViewModel
import kotlinx.android.synthetic.main.activity_epub_viewer.*


class EpubViewer : AppCompatActivity() {
    private lateinit var viewModel: EpubViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val gameBinding: ActivityEpubViewerBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_epub_viewer)
        viewModel = ViewModelProvider(this).get(EpubViewerViewModel::class.java)
        gameBinding.viewModel = viewModel
        gameBinding.lifecycleOwner = this

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar)


        //TODO: NEED TO MOVE IT TO VIEWMODEL
        seekBar.hintDelegate
            .setHintAdapter { _, progress -> "Progress: $progress" }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_epub, menu)
        return true
    }

}