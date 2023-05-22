package com.amorphteam.ketub.ui.epub

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
import androidx.appcompat.app.AppCompatActivity
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
import com.amorphteam.ketub.ui.epub.fragments.search.SearchSingleFragment
import com.amorphteam.ketub.ui.epub.fragments.StyleListener
import com.amorphteam.ketub.ui.epub.fragments.toc.TocSingleFragment
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkFragment
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkViewModel
import com.amorphteam.ketub.ui.main.tabs.bookmark.tabs.first_and_second.BookmarkViewModelFactory
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mehdok.fineepublib.epubviewer.epub.Book
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import com.mehdok.fineepublib.epubviewer.epub.NavPoint
import kotlinx.android.synthetic.main.bottom_sheet_style.*
import java.security.Key
import java.util.*


class EpubActivity : AppCompatActivity() {
    lateinit var binding: ActivityEpubBinding
    lateinit var viewModel: EpubViewModel
    private var sheetBehavior: BottomSheetBehavior<*>? = null
    lateinit var bookAddress:String
    lateinit var bookName: String
    lateinit var bookPath: String
    private var hideHandler = Handler(Looper.myLooper()!!)
    private var navIndex = -1
    var navUri:String? = null
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
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.spineArray.observe(this) {
            ifFromReferences(navIndex >= 0 || navUri !=null)
            handleViewEpubPager(it)
            handleBookNameAndBookPath()
        }

        viewModel.dismissSheet.observe(this){
                sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                binding.bg.visibility = View.GONE
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
            viewModel.getBookAddress(bookAddress)
        }

        val prefManager = PreferencesManager(this)
        viewModel.setPrefManage(prefManager)


        viewModel.handleSavedStyle()

        EpubVerticalDelegate.subscribeOn(this)

    }

    private fun handleBookNameAndBookPath() {
        bookPath = bookAddress.split("/").last()
        bookName = BookHolder.instance?.jsBook?.bookName.toString()
    }

    private fun ifFromReferences(status:Boolean) {
        if (status) {
            if (navUri == null) {
                viewModel.handleBookmarkPage(navIndex)
            }else{
                viewModel.handleNavUriPage(navUri)
            }
        } else {
            viewModel.handleLastPageSeen(bookAddress)
        }
    }

    private fun handleChipsView() {
        viewModel.setChips(chipGroup, Keys.FONT_ARRAY)
    }

    fun addStyleListener(styleListener: StyleListener){
        viewModel.styleListener!!.add(styleListener)
    }


    fun removeStyleListener(styleListener: StyleListener){
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
        addPagerScrollListener(binding.epubVerticalViewPager)
        setUpChapterSeeker(spineItems.size)
        viewModel.lastPageSeen.value?.let { moveToPage(it) }
    }

    private fun addPagerScrollListener(pager: ViewPager2) {
        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.seekBar.progress = position
            }
        })
    }

    private fun setUpChapterSeeker(maxPage: Int) {
        binding.seekBar.max = maxPage
        binding.seekBar.hintDelegate.setHintAdapter { p0, p1 -> "$p1" }
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.pageNumber.text = String.format(
                    Locale.getDefault(),
                    "%d / %d",
                    (p0?.progress ?: 0) + 1,
                    BookHolder.instance?.jsBook?.pageNumber
                )
                viewModel.preferencesManager.saveLastPageSeen(bookAddress,p0?.progress ?: 0)

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
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_epub, menu)
        return true
    }

    fun bookmarkCurrentPageHelper(){
        val firstWord = Book.resourceName2Url(adapter!!.getCurrentFragment()?.manifestItem?.href).toString()
        val nearestTitle:String? = BookHolder.instance?.jsBook?.getNavTitle(firstWord)
        Log.i(Keys.LOG_NAME, nearestTitle!!)

        viewModel.bookmarkCurrentPage(bookPath, bookName, binding.epubVerticalViewPager.currentItem, " علامة مرجعية ${binding.epubVerticalViewPager.currentItem} $nearestTitle")
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
                handleFragment(TocSingleFragment())
                true
            }

            R.id.bookmark -> {
                handleFragment(BookmarkFragment(singleBookName = bookName))
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
        handleChipsView()
        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.1F
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }



}