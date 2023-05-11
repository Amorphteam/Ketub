package com.amorphteam.ketub.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityMainBinding
import com.amorphteam.ketub.utility.FileManager
import com.amorphteam.ketub.utility.Keys
import com.google.android.material.bottomnavigation.BottomNavigationView

class TabbedActivity : AppCompatActivity() {

    private val fileManager = FileManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProvider(this).get(TabbedViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (fileManager.isNewVersion(this)) {
            copyAppContentToUserDoc()
        } else {
            Log.i(Keys.LOG_NAME, "fileManager is existed")
        }

        initNavigationBar()
    }


    private fun copyAppContentToUserDoc() {
        Thread {
            fileManager.copyBooksToUserDoc(this)
            fileManager.copyCoversToUSerDoc(this)

        }.start()
    }

    private fun initNavigationBar() {
        //Initialize Bottom Navigation View.
        val navView = findViewById<BottomNavigationView>(R.id.bottomNav_view)

        //Initialize NavController.
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navView, navController)
    }

}