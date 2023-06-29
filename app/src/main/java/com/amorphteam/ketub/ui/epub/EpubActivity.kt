package com.amorphteam.ketub.ui.epub

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.reference.ReferenceDatabase
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.databinding.ActivityEpubBinding
import com.amorphteam.ketub.model.BookHolder
import com.amorphteam.ketub.ui.adapter.EpubVerticalAdapter
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkFragment
import com.amorphteam.ketub.ui.main.tabs.toc.tabs.first_and_second.TocFragment
import com.amorphteam.ketub.ui.search.SearchActivity
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.mehdok.fineepublib.epubviewer.epub.Book
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import kotlinx.android.synthetic.main.bottom_sheet_style.*
import java.security.Key
import java.util.*


class EpubActivity : AppCompatActivity() {
    lateinit var binding: ActivityEpubBinding
    lateinit var viewModel: EpubViewModel
    private var sheetBehavior: BottomSheetBehavior<*>? = null
    lateinit var bookAddress: String
    lateinit var bookPath: String
    var searchWord: String? = null
    private var hideHandler = Handler(Looper.myLooper()!!)
    private var navIndex = -1
    var navUri: String? = null
    var isFromSearch = false
    var adapter:
            EpubVerticalAdapter? = null
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


        val application = requireNotNull(this).application
        val referenceDao = ReferenceDatabase.getInstance(application).referenceDatabaseDao
        val referenceRepository = ReferenceRepository(referenceDao)
        val viewModelFactory = EpubViewModelFactory(referenceRepository)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[EpubViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.toolbar.title = " "
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.spineArray.observe(this) {
            ifFromReferences(navIndex >= 0 || navUri != null)
            handleViewEpubPager(it)
            handleBookNameAndBookPath()
        }

        viewModel.dismissSheet.observe(this) {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            binding.bg.visibility = View.GONE
        }

        viewModel.bookmarkSelected.observe(this) {

            if (it == false) {
                addBookmark()
            }

        }

        viewModel.fullScreen.observe(this) {
            if (it) {
                hide()
            } else {
                show()
            }
        }

        if (intent.extras != null) {
            bookAddress = intent.getStringExtra(Keys.BOOK_ADDRESS)!!
            navIndex = intent.getIntExtra(Keys.NAV_INDEX, -1)
            navUri = intent.getStringExtra(Keys.NAV_URI)
            searchWord = intent.getStringExtra(Keys.SEARCH_WORD)

            // If searchWord not null; then its coming from search activity
            if (searchWord != null){
                isFromSearch = true
            }
            viewModel.getBookAddress(bookAddress)
        }



        val prefManager = PreferencesManager(this)
        viewModel.setPrefManage(prefManager)


        viewModel.handleSavedStyle()

        EpubVerticalDelegate.subscribeOn(this)

    }

    private fun addBookmark() {
        val firstWord =
            Book.resourceName2Url(adapter!!.getCurrentFragment()?.manifestItem?.href).toString()
        var nearestTitle: String? = BookHolder.instance?.jsBook?.getNavTitle(firstWord)
        if (nearestTitle.isNullOrEmpty()){
            nearestTitle = getString(R.string.bookmark_item_title) + " " + (binding.epubVerticalViewPager.currentItem+1)
        }

        viewModel.bookmarkCurrentPage(
            bookPath,
            viewModel.bookName.value.toString(),
            binding.epubVerticalViewPager.currentItem,
            nearestTitle
        )
    }

    private fun handleBookNameAndBookPath() {
        bookPath = bookAddress.split("/").last()
        viewModel.bookName.value = BookHolder.instance?.jsBook?.bookName.toString()
    }

    private fun ifFromReferences(status: Boolean) {
        if (status) {
            if (navUri == null) {
                viewModel.handleBookmarkPage(navIndex)
            } else {
                viewModel.handleNavUriPage(navUri)
            }
        } else {
            viewModel.handleLastPageSeen(bookAddress)
        }
    }

    private fun handleChipsView() {
        viewModel.setChips(chipGroup, Keys.FONT_ARRAY)
    }

    fun addStyleListener(styleListener: StyleListener) {
        viewModel.styleListener!!.add(styleListener)
    }


    fun removeStyleListener(styleListener: StyleListener) {
        viewModel.styleListener!!.remove(styleListener)
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.saveStyles()
        viewModel.styleListener!!.clear()
        viewModel.styleListener = null
        BookHolder.instance?.jsBook = null
        EpubVerticalDelegate.unSubscribe(this)
    }

    private fun handleViewEpubPager(spineItems: ArrayList<ManifestItem>) {
        adapter =
            EpubVerticalAdapter(spineItems, this.supportFragmentManager, lifecycle)
        binding.epubVerticalViewPager.adapter = adapter
        binding.epubVerticalViewPager.offscreenPageLimit = Keys.MAX_SIDE_PAGE
        viewModel.lastPageSeen.value?.let { moveToPage(it) }
        addPagerScrollListener(binding.epubVerticalViewPager)
        setUpChapterSeeker(spineItems.size)
    }

    private fun addPagerScrollListener(pager: ViewPager2) {
        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.seekBar.progress = position+1
            }
        })
    }

    private fun setUpChapterSeeker(maxPage: Int) {
        binding.seekBar.max = maxPage-1
        binding.seekBar.hintDelegate.setHintAdapter { _, p1 -> "$p1" }
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.pageNumber.value = String.format(
                    Locale.getDefault(),
                    "%d / %d",
                    p0?.progress?.plus(1),
                    maxPage
                )
                viewModel.preferencesManager.saveLastPageSeen(bookAddress, p0?.progress?.plus(1) ?: 1)

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
        viewModel.setFullScreenWindow()
        transaction.add(R.id.fragment_container, newFragment)
        transaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_epub, menu)
        return true
    }

    fun bookmarkCurrentPageHelper() {

        viewModel.checkBookmark(
            viewModel.bookName.value.toString(),
            binding.epubVerticalViewPager.currentItem
        )

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
                handleFragment(TocFragment(singleBookPath = bookPath))
                true
            }

            R.id.bookmark -> {
                handleFragment(BookmarkFragment(singleBookName = viewModel.bookName.value!!))
                true
            }

            R.id.search -> {
                openSearchActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSearchActivity() {
        viewModel.saveStyles()
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(Keys.SINGLE_BOOK_PATH, bookPath)
        startActivity(intent)
    }

    private fun handleStyleSheet() {
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
    }

    private fun openStyleSheet() {
        handleStyleSheet()
        handleChipsView()
        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.1F
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }


}