package com.amorphteam.ketub.ui.epub

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ActivityEpubViewerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_epub_viewer.*


class EpubViewer : AppCompatActivity() {
    private lateinit var viewModel: EpubViewerViewModel
    private lateinit var toolbar: Toolbar
    var toggle: Boolean = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    @SuppressLint("ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityEpubViewerBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_epub_viewer)
        viewModel = ViewModelProvider(this).get(EpubViewerViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar)

        //TODO: NEED TO MOVE IT TO VIEWMODEL
        seekBar.hintDelegate
            .setHintAdapter { _, progress -> "Progress: $progress" }

        val gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            override fun onDoubleTap(event: MotionEvent): Boolean {
                toggleToolbar(toggle)
                return true
            }
        })

        webView.setBackgroundColor(Color.parseColor(resources.getString(R.color.background2)))
        webView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
        }

    }

    @SuppressLint("ResourceType")
    private fun handleStyleSheet() {

        val bottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 440
        handleIconBaseOnTheme()
        bg.visibility = View.VISIBLE;
        bg.alpha = 0.3f

    }

    private fun handleIconBaseOnTheme() {
        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                viewModel.darkTheme.value = true
                viewModel.lightTheme.value = false
                viewModel.basetTheme.value = false
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                viewModel.darkTheme.value = false
                viewModel.lightTheme.value = true
                viewModel.basetTheme.value = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                viewModel.darkTheme.value = false
                viewModel.lightTheme.value = false
                viewModel.basetTheme.value = true
            }
        }
    }


    private fun openStyleSheet() {
        handleStyleSheet()
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                bottomSheetBehavior.peekHeight = 0
                if (i == STATE_COLLAPSED)
                    bg.visibility = View.GONE;

            }

            override fun onSlide(view: View, v: Float) {
                bg.visibility = View.VISIBLE;
                bg.alpha = v;
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                openStyleSheet()
                true
            }

            android.R.id.home -> {
                finish()
                true
            }

            R.id.search -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleToolbar(status: Boolean) {
        if (status) {
            page_number.setTextColor(resources.getColor(R.color.onbackground2))
            webView_countiner.cardElevation = 0f
            main_epub_countiner.setBackgroundColor(resources.getColor(R.color.background2))
            slider_countiner.visibility = View.GONE
            book_name.text = resources.getText(R.string.book_name)
            book_name.visibility = View.VISIBLE
            toolbar.navigationIcon = null
            window.statusBarColor = ContextCompat.getColor(this, R.color.background2)
            toggle = false
        } else {
            page_number.setTextColor(resources.getColor(R.color.primary2))
            webView_countiner.cardElevation = 4f
            main_epub_countiner.setBackgroundColor(resources.getColor(R.color.background1))
            slider_countiner.visibility = View.VISIBLE
            book_name.visibility = View.GONE
            toolbar.setNavigationIcon(R.drawable.ic_back)
            window.statusBarColor = ContextCompat.getColor(this, R.color.background1)
            toggle = true
        }
        invalidateOptionsMenu()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_epub, menu)
        if (!toggle) {
            for (i in 0 until menu!!.size()) menu.getItem(i).isVisible = false
        } else {
            for (i in 0 until menu!!.size()) menu.getItem(i).isVisible = true
        }
        return true
    }

}