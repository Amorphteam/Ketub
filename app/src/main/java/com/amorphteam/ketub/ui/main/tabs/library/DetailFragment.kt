package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.os.Bundle
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
import com.amorphteam.ketub.ui.adapter.BookAdapter
import com.amorphteam.ketub.ui.adapter.BookClickListener
import com.amorphteam.ketub.database.book.BookDatabase
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.model.CatSection
import com.amorphteam.ketub.utility.Keys

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var catSection: CatSection
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (arguments != null) {
            catSection = (arguments?.getSerializable(Keys.NAV_CAT_SECTION) as? CatSection)!!
        }
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao
        val bookRepository = BookRepository(bookDao)
        val viewModelFactory = DetailViewModelFactory(bookRepository, catSection)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.bookItems.observe(viewLifecycleOwner){
            if (it.size == 1) {
                viewModel.openEpubAct()
            }else{
                //TODO THIS SECTION MUST BE COMPLETED
            }
        }

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }

        viewModel.startLibraryFrag.observe(viewLifecycleOwner) {
            if (it) Navigation.findNavController(requireView())
                .navigate(R.id.action_detailFragment_to_navigation_library)
        }

        viewModel.allCats.observe(viewLifecycleOwner) {
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

    private fun handleCatBooks(bookArrayList: List<CategoryModel>) {
        val adapter = BookAdapter(BookClickListener { bookId ->
            viewModel.getCatId(bookId)
        })
        adapter.submitList(bookArrayList)

        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }


}