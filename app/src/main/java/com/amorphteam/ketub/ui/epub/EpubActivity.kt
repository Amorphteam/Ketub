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
import com.amorphteam.ketub.utility.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import kotlinx.android.synthetic.main.bottom_sheet_style.*
import kotlinx.android.synthetic.main.item_group_index.*
import java.util.*


class EpubActivity : AppCompatActivity() {
    lateinit var binding: ActivityEpubBinding
    lateinit var viewModel: EpubViewModel
    private var sheetBehavior: BottomSheetBehavior<*>? = null

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
        viewModel = ViewModelProvider(this).get(EpubViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        viewModel.spineArray.observe(this) {
            handleViewEpubPager(it)

        }

        viewModel.fullScreen.observe(this) {
            if (it) {
                hide()
            } else {
                show()
            }
        }
        if (intent.extras != null) {
            val bookAddress = intent.getStringExtra(Keys.BOOK_ADDRESS)
            val navPoint = intent.getIntExtra(Keys.NAV_POINT, 0)
            viewModel.getBookAddress(bookAddress)
        }

        val prefManager = PreferencesManager(this)
        viewModel.handleSavedStyle(prefManager)
    }



    private fun hide() {
        hideHandler.removeCallbacks(showRunnable)
        hideHandler.postDelayed(hideRunnable, Keys.UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
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
        viewModel.setAdapter(adapter)
        binding.epubVerticalViewPager.offscreenPageLimit = Keys.MAX_SIDE_PAGE
        addPagerScrollListener(binding.epubVerticalViewPager)
        setUpChapterSeeker(spineItems.size, 0)
    }

    private fun addPagerScrollListener(pager: ViewPager2) {
        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.seekBar.progress = position
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
                openStyleSheet()
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

    private fun handleStyleSheet() {
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
    }
    private fun openStyleSheet() {
        handleStyleSheet()
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                sheetBehavior?.peekHeight = 0
//                if (i == BottomSheetBehavior.STATE_COLLAPSED)
//                    binding.bg.visibility = View.GONE;

            }

            override fun onSlide(view: View, v: Float) {
//                binding.bg.visibility = View.VISIBLE;
//                binding.bg.alpha = v;
            }
        })

    }



}