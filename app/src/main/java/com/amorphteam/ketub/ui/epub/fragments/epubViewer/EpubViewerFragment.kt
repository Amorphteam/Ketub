package com.amorphteam.ketub.ui.epub.fragments.epubViewer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentEpubViewBinding
import com.amorphteam.ketub.model.BookHolder
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.utility.Keys
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mehdok.fineepublib.epubviewer.epub.Book
import com.mehdok.fineepublib.epubviewer.epub.ManifestItem
import com.mehdok.fineepublib.epubviewer.jsepub.client.JsPictureListener
import com.mehdok.fineepublib.epubviewer.jsepub.client.JsPictureListener.WebViewPictureListener
import com.mehdok.fineepublib.interfaces.EpubScrollListener
import com.mehdok.fineepublib.interfaces.EpubTapListener
import kotlinx.android.synthetic.main.sheet.*


class EpubViewerFragment : Fragment(), WebViewPictureListener, EpubTapListener, EpubScrollListener {
    private lateinit var binding: FragmentEpubViewBinding
    private lateinit var viewModel: EpubViewerViewModel
    var toggle: Boolean = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var navController: NavController
    lateinit var webView: LocalWebView
    private var jsPictureListener: JsPictureListener? = null

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
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        showLoadProgress(true)

        viewModel.htmlSourceString.observe(viewLifecycleOwner) {
            if (it != null) {
                fillWebView(it)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val manifestItem: ManifestItem? = requireArguments().getParcelable(Keys.MANIFEST_ITEM)
            val position = requireArguments().getInt(Keys.POSITION_ITEM)
            viewModel.getResourceString(
                requireContext(),
                Book.resourceName2Url(manifestItem?.href),
                "textSizeThree",
                position
            )
        }
    }


    private fun fillWebView(it: String) {
        webView = LocalWebView(requireContext())
        jsPictureListener = JsPictureListener(this)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        webView.setTapListener(this)
        webView.setScrollListener(this)
        webView.setPictureListener(jsPictureListener)
        webView.setBook(BookHolder.instance?.jsBook)
        webView.loadResourcePage(it)
        webView.layoutParams = params
        binding.mainEpubContainer.addView(webView)
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
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.background2)
        } else {
            viewModel.hideToolbar.value = false
            toggle = true
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
//        binding.bg.visibility = View.VISIBLE;
//        binding.bg.alpha = 0.3f

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
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                bottomSheetBehavior.peekHeight = 0
//                if (i == BottomSheetBehavior.STATE_COLLAPSED)
//                    binding.bg.visibility = View.GONE;

            }

            override fun onSlide(view: View, v: Float) {
//                binding.bg.visibility = View.VISIBLE;
//                binding.bg.alpha = v;
            }
        })


    }

    private fun showLoadProgress(flag: Boolean) {
        //sometimes if you scroll too fast like crazy one this might be null
        if (flag) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.animate().setDuration(200).alpha(1f).start()
        } else {
            binding.progressBar.animate().setDuration(200).alpha(0f).start()
            binding.progressBar.setVisibility(View.GONE)
        }
    }

    companion object {
        fun newInstance(
            manifestItem: ManifestItem?,
            pos: Int
        ): EpubViewerFragment {
            val bundle = Bundle()
            bundle.putParcelable(Keys.MANIFEST_ITEM, manifestItem)
            bundle.putInt(Keys.POSITION_ITEM, pos)
            val instance: EpubViewerFragment =
                EpubViewerFragment()
            instance.arguments = bundle
            return instance
        }

    }

    override fun onDrawingFinished() {
        // this method calls every time anythings happens in webview that require a refresh like selecting text
        //TODO: MUST HANDLE IT
        showLoadProgress(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (jsPictureListener != null) {
            jsPictureListener?.setWebViewPictureListener(null)
        }
    }

    override fun onPageScrolled(scrollY: Int) {

    }

    override fun onEmailTapped(email: String?) {
        Toast.makeText(requireContext(), email, Toast.LENGTH_SHORT).show()
    }

    override fun onWebTapped(link: String?) {
        if (!link!!.startsWith("http://localhost")) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
    }

    override fun onEmptySpaceTapped() {
        val activity = requireActivity() as EpubActivity
        activity.viewModel.toggle()
    }

    override fun onExternalLinkCLicked(uri: Uri?) {
        if (!uri.toString().startsWith("http://localhost")) {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }

    override fun getHitTestResult(): WebView.HitTestResult {
        return webView.hitTestResult
    }

    override fun onPageScrolled() {

    }


}