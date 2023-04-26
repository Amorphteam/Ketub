package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentLibraryBinding
import com.amorphteam.ketub.ui.epub.EpubViewer
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookClickListener
import com.amorphteam.ketub.ui.main.tabs.library.adapter.MainTocAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.MainTocClickListener
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.ui.search.SearchActivity
import com.amorphteam.ketub.utility.Keys


class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var viewModel: LibraryFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(LibraryFragmentViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_library, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        }

        viewModel.startSearchAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, SearchActivity::class.java))
        }

        viewModel.startDetailFrag.observe(viewLifecycleOwner) {
            if (it) Navigation.findNavController(requireView()).navigate(R.id.action_navigation_library_to_detailFragment)
        }

        viewModel.readMoreToc.observe(viewLifecycleOwner) {
            handleReadMore(it)
        }

        viewModel.errorTocRecieve.observe(viewLifecycleOwner) {
            Log.i(Keys.LOG_NAME, it)
        }

        viewModel.recommendedToc.observe(viewLifecycleOwner) {
            handleRecommanded(it)
        }

        viewModel.errorTocRecieve.observe(viewLifecycleOwner) {
            Log.i(Keys.LOG_NAME, it)
        }

        handleBooksRecyclerView()
        return binding.root
    }

    private fun handleRecommanded(list: List<MainToc>) {
        val recommandedToc = MainTocAdapter(MainTocClickListener {
            viewModel.openEpubAct()
        })
        recommandedToc.submitList(list)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocRecommanded.recyclerView.layoutManager = layoutManager
        binding.tocRecommanded.recyclerView.adapter = recommandedToc
    }

    private fun handleReadMore(list: List<MainToc>) {
        val readMoreToc = MainTocAdapter(MainTocClickListener {
            viewModel.openEpubAct()
        })
        readMoreToc.submitList(list)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocReadMore.recyclerView.layoutManager = layoutManager
        binding.tocReadMore.recyclerView.adapter = readMoreToc
    }

    private fun handleBooksRecyclerView() {
        handleEjtihad()
        handleNosos()
    }

    private fun handleNosos() {
        val nososAdapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        nososAdapter.submitList(viewModel.getNososItem().value)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.nososItems.recyclerView.layoutManager = layoutManager
        binding.nososItems.recyclerView.adapter = nososAdapter
    }

    private fun handleEjtihad() {
        val ejtihadAdapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        ejtihadAdapter.submitList(viewModel.getEjtihadItem().value)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.ejtehadItems.recyclerView.layoutManager = layoutManager
        binding.ejtehadItems.recyclerView.adapter = ejtihadAdapter
    }


}