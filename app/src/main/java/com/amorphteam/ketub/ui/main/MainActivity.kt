package com.amorphteam.ketub.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.Navigation
import com.amorphteam.ketub.R
import kotlinx.android.synthetic.main.fragment_library.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initNavigationBar()
    }



    private fun initNavigationBar() {
        //Initialize Bottom Navigation View.
        val navView = findViewById<BottomNavigationView>(R.id.bottomNav_view)

        //Pass the ID's of Different destinations
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_library,
            R.id.navigation_toc_list
        )
            .build()

        //Initialize NavController.
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navView, navController)
    }

}