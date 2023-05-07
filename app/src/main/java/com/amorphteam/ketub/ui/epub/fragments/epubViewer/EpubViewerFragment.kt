package com.amorphteam.ketub.ui.epub.fragments.epubViewer

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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentEpubViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.sheet.*


class EpubViewerFragment : Fragment() {
    private lateinit var binding: FragmentEpubViewBinding
    private lateinit var viewModel: EpubViewerViewModel
    var toggle: Boolean = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var navController: NavController


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_epub_view, container, false
        )
        viewModel = ViewModelProvider(this)[EpubViewerViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.menu_epub)
        toolbar.setOnMenuItemClickListener { menuItem ->
            // Handle menu item clicks here
            when (menuItem.itemId) {
                R.id.setting -> {
                    openStyleSheet()
                    true
                }
                R.id.home -> {
                    requireActivity().finish()
                    true
                }

                R.id.toc -> {
                    openTocFragment()
                    true
                }

                R.id.bookmark -> {
                    openBookmarkFragment()
                    true
                }
                R.id.search -> {
                    openSearchFragment()
                    true
                }

                else -> false
            }
        }


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

    private fun openSearchFragment() {
        navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_epubViewFragment_to_searchSingleFragment)
    }

    private fun openBookmarkFragment() {
        navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_epubViewFragment_to_bookmarkSingleFragment)
    }
    private fun toggleToolbar(status: Boolean) {
        if (status) {
            viewModel.hideToolbar.value = true
            toggle = false
            binding.toolbar.visibility = View.INVISIBLE
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.background2)
        } else {
            viewModel.hideToolbar.value = false
            toggle = true
            binding.toolbar.visibility = View.VISIBLE
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.background1)
        }
        invalidateOptionsMenu(requireActivity())

    }

    //TODO: SHOULD MOVE TO VIEWMODEL
    private fun openTocFragment() {
        navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_epubViewFragment_to_tocSingleFragment)
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