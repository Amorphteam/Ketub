package com.amorphteam.ketub.ui.epub.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.sheet.*


class EpubViewFragment : Fragment() {
    private lateinit var binding: com.amorphteam.ketub.databinding.FragmentEpubViewBinding
    private lateinit var viewModel: EpubViewFragmentViewModel
    var toggle: Boolean = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_epub_view, container, false
        )
        viewModel = ViewModelProvider(this)[EpubViewFragmentViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.toolbar.title = ""
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);


        val gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(event: MotionEvent): Boolean {
                toggleToolbar(toggle)
                return true
            }
        })

        binding.webView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }


        return binding.root
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                openStyleSheet()
                true
            }

            android.R.id.home -> {
                requireActivity().finish()
                true
            }

            R.id.search -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_epub, menu)
        if (!toggle) {
            for (i in 0 until menu.size()) menu.getItem(i).isVisible = false
        } else {
            for (i in 0 until menu.size()) menu.getItem(i).isVisible = true
        }
    }

    private fun toggleToolbar(status: Boolean) {
        if (status) {
            viewModel.hideToolbar.value = true
            toggle = false
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.background2)
        } else {
            viewModel.hideToolbar.value = false
            toggle = true
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.background1)
        }
        invalidateOptionsMenu(requireActivity())
    }


    private fun handleStyleSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.peekHeight = 440
        handleIconBaseOnTheme()
        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.3f

    }

    private fun handleIconBaseOnTheme() {
        when (resources.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
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
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                bottomSheetBehavior.peekHeight = 0
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                    binding.bg.visibility = View.GONE;

            }

            override fun onSlide(view: View, v: Float) {
                binding.bg.visibility = View.VISIBLE;
                binding.bg.alpha = v;
            }
        })


    }


}