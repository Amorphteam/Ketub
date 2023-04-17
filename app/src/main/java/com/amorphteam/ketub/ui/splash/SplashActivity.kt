package com.amorphteam.ketub.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivitySplashBinding
import com.amorphteam.ketub.ui.main.TabbedActivity


class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, TabbedActivity::class.java)
            this.startActivity(mainIntent)
            this.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
        
    }


}