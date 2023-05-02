package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentDetailBinding
import com.amorphteam.ketub.ui.epub.EpubActivity
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookClickListener
import com.amorphteam.ketub.ui.main.tabs.library.database.BookDatabase
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.TitleAndDes
import com.amorphteam.ketub.utility.Keys

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var titleAndDes: TitleAndDes? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (arguments != null) {
            titleAndDes = arguments?.getSerializable("titleAndDes") as? TitleAndDes
        }
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = BookDatabase.getInstance(application).bookDatabaseDao
        val viewModelFactory = DetailViewModelFactory(dataSource, titleAndDes!!)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }

        viewModel.startLibraryFrag.observe(viewLifecycleOwner) {
            if (it) Navigation.findNavController(requireView())
                .navigate(R.id.action_detailFragment_to_navigation_library)
        }

        viewModel.books.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                handleCatBooks(it)
            }
        }


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.openLibraryFrag()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)

        return binding.root
    }

    private fun handleCatBooks(bookArrayList: List<BookModel>) {
        val adapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        adapter.submitList(bookArrayList)

        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }


}