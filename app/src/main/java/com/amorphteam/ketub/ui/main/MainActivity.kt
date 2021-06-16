package com.amorphteam.ketub.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityMainBinding
import com.amorphteam.ketub.ui.epub.EpubViewer

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        gameBinding.viewModle = viewModel
        gameBinding.lifecycleOwner = this

        viewModel.startEpubAct.observe(this, {
            if (it) startActivity(Intent(this, EpubViewer::class.java))
        })
    }
}