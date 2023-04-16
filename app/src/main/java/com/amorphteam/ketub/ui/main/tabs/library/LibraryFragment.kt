package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentLibraryBinding
import com.amorphteam.ketub.ui.epub.EpubViewer
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.BookClickListener
import com.amorphteam.ketub.ui.main.tabs.library.adapter.MainTocAdapter
import com.amorphteam.ketub.ui.main.tabs.library.adapter.MainTocClickListener


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

        handleBooksRecyclerView()
        handleMainTocsRecyclerView()
        return binding.root
    }

    private fun handleMainTocsRecyclerView() {
        handleReadMore()
        handleRecommanded()
    }

    private fun handleRecommanded() {
        val recommandedToc = MainTocAdapter(MainTocClickListener {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        })
        recommandedToc.submitList(viewModel.getRecommandedToc().value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocRecommanded.recyclerView.layoutManager = layoutManager
        binding.tocRecommanded.recyclerView.adapter = recommandedToc
    }

    private fun handleReadMore() {
        val readMoreToc = MainTocAdapter(MainTocClickListener {
            Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        })
        readMoreToc.submitList(viewModel.getReadMoreMainToc().value)
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
            Toast.makeText(context, "$bookId", Toast.LENGTH_SHORT).show()
        })
        nososAdapter.submitList(viewModel.getNososItem().value)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.nososItems.recyclerView.layoutManager = layoutManager
        binding.nososItems.recyclerView.adapter = nososAdapter
    }

    private fun handleEjtihad() {
        val ejtihadAdapter = BookAdapter(BookClickListener { bookId ->
            Toast.makeText(context, "$bookId", Toast.LENGTH_SHORT).show()
        })
        ejtihadAdapter.submitList(viewModel.getEjtihadItem().value)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.ejtehadItems.recyclerView.layoutManager = layoutManager
        binding.ejtehadItems.recyclerView.adapter = ejtihadAdapter
    }


}