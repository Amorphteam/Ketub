package com.amorphteam.ketub.ui.epub

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubBinding
import com.amorphteam.ketub.ui.epub.fragments.epubViewer.EpubContainerFragment
import com.amorphteam.ketub.utility.Keys


class EpubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEpubBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_epub)
        val viewModel = ViewModelProvider(this).get(EpubViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.spineArray.observe(this){

                val newFragment: Fragment = EpubContainerFragment.newInstance(it, 0)
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                transaction.replace(R.id.fragment_container, newFragment)
                transaction.addToBackStack(null)
                transaction.commit()

        }
        if (intent.extras != null) {
            val bookAddress = intent.getStringExtra(Keys.BOOK_ADDRESS)
            val navPoint = intent.getIntExtra(Keys.NAV_POINT, 0)
            viewModel.getBookAddress(bookAddress)

        }



    }


}