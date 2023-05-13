package com.amorphteam.ketub.ui.epub

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubBinding
import com.amorphteam.ketub.model.BookHolder
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.ui.epub.fragments.search.SearchSingleFragment
import com.amorphteam.ketub.utility.Keys
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import kotlinx.android.synthetic.main.activity_epub.*
import java.util.*


class EpubActivity : AppCompatActivity() {
    lateinit var binding: ActivityEpubBinding
    private var isFullScreen = false

    private var hideHandler = Handler(Looper.myLooper()!!)
    private val showRunnable = Runnable {
        supportActionBar?.show()
    }

    private val hideRunnable = Runnable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.epubVerticalViewPager.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            binding.epubVerticalViewPager.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_epub)
        val viewModel = ViewModelProvider(this).get(EpubViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        isFullScreen = true
        binding.pageNumber.setOnClickListener {
            toggle()
        }
        setSupportActionBar(binding.toolbar)

        viewModel.spineArray.observe(this) {
            handleViewEpubPager(it)

        }
        if (intent.extras != null) {
            val bookAddress = intent.getStringExtra(Keys.BOOK_ADDRESS)
            val navPoint = intent.getIntExtra(Keys.NAV_POINT, 0)
            viewModel.getBookAddress(bookAddress)

        }


    }

    fun toggle() {
        if (isFullScreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        supportActionBar?.hide()
        isFullScreen = false
        binding.seekBar.visibility = View.GONE
        hideHandler.removeCallbacks(showRunnable)
        hideHandler.postDelayed(hideRunnable, Keys.UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        isFullScreen = true
        binding.seekBar.visibility = View.VISIBLE

        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(showRunnable, Keys.UI_ANIMATION_DELAY.toLong())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(Keys.UI_ANIMATION_DELAY)
    }

    private fun delayedHide(uiAnimationDelay: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, uiAnimationDelay.toLong())
    }

    private fun handleViewEpubPager(spineItems: ArrayList<ManifestItem>) {
        val adapter =
            EpubVerticalAdapter(spineItems, this.supportFragmentManager, lifecycle)
        binding.epubVerticalViewPager.adapter = adapter
        binding.epubVerticalViewPager.offscreenPageLimit = Keys.MAX_SIDE_PAGE
        addPagerScrollListener(binding.epubVerticalViewPager)
        setUpChapterSeeker(spineItems.size, 0)

    }

    private fun addPagerScrollListener(pager: ViewPager2) {
        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.seekBar.progress = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    private fun setUpChapterSeeker(maxPage: Int, currentPage: Int) {
        binding.seekBar.max = maxPage
        binding.seekBar.progress = currentPage

        binding.seekBar.hintDelegate.setHintAdapter { p0, p1 -> "$p1" }
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.pageNumber.text = String.format(
                    Locale.getDefault(),
                    "%d / %d",
                    (p0?.progress ?: 0) + 1,
                    BookHolder.instance?.jsBook?.pageNumber
                )
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.progress?.let { moveToPage(it) }
            }

        })
    }

    private fun moveToPage(page: Int) {
        binding.epubVerticalViewPager.setCurrentItem(page, false)
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