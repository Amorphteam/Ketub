package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
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
import com.amorphteam.ketub.utility.Keys

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val dataSource = BookDatabase.getInstance(application).bookDatabaseDao
        val viewModelFactory = DetailViewModelFactory(dataSource)

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

        viewModel.firstCatBooksAllItems.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Log.i(Keys.LOG_NAME, "handleFirstCatBooks")
                handleFirstCatBooks(it)
            }
        }

        viewModel.secondCatBooksAllItems.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Log.i(Keys.LOG_NAME, "handleSecondCatBooks")
                handleSecondCatBooks(it)
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

    private fun handleFirstCatBooks(bookArrayList: List<BookModel>) {
        val adapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        adapter.submitList(bookArrayList)

        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    private fun handleSecondCatBooks(bookArrayList: List<BookModel>) {
        val adapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        adapter.submitList(bookArrayList)

        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

}