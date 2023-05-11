package com.amorphteam.ketub.ui.epub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubBinding
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkViewModelFactory


class EpubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEpubBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_epub)
        val viewModel = ViewModelProvider(this).get(EpubViewModel::class.java)

        if (intent.extras != null) {
            val bookAddress = intent.getStringExtra(Keys.BOOK_ADDRESS)
            val navPoint = intent.getIntExtra(Keys.NAV_POINT, 0)
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }


}