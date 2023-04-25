package com.amorphteam.ketub.ui.main.tabs.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.search.SearchActivity
import com.amorphteam.ketub.utility.Keys
import java.io.File
import java.io.IOException


class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var viewModel: LibraryFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(LibraryFragmentViewModel::class.java)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(LibraryFragmentViewModel::class.java)
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


        viewModel.allBooks.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                addAssetsImages(requireActivity().application, "cover")
                handleNosos(it)
//                handleEjtihad(it)
                Log.i("sssssss", it.toString())

            }
        })


//        handleBooksRecyclerView()
        handleMainTocsRecyclerView()
        return binding.root
    }

    private fun handleMainTocsRecyclerView() {
        handleReadMore()
        handleRecommanded()
    }

    private fun handleRecommanded() {
        val recommandedToc = MainTocAdapter(MainTocClickListener {
            viewModel.openEpubAct()
        })
        recommandedToc.submitList(viewModel.getRecommandedToc().value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocRecommanded.recyclerView.layoutManager = layoutManager
        binding.tocRecommanded.recyclerView.adapter = recommandedToc
    }

    private fun handleReadMore() {
        val readMoreToc = MainTocAdapter(MainTocClickListener {
            viewModel.openEpubAct()
        })
        readMoreToc.submitList(viewModel.getReadMoreMainToc().value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.tocReadMore.recyclerView.layoutManager = layoutManager
        binding.tocReadMore.recyclerView.adapter = readMoreToc
    }


    private fun handleNosos(bookArrayList : List<BookModel>) {
        val nososAdapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        nososAdapter.submitList(bookArrayList)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.nososItems.recyclerView.layoutManager = layoutManager
        binding.nososItems.recyclerView.adapter = nososAdapter
    }

    private fun handleEjtihad(bookArrayList : List<BookModel>) {
        val ejtihadAdapter = BookAdapter(BookClickListener { bookId ->
            viewModel.openEpubAct()
        })
        ejtihadAdapter.submitList(bookArrayList)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.ejtehadItems.recyclerView.layoutManager = layoutManager
        binding.ejtehadItems.recyclerView.adapter = ejtihadAdapter
    }

    fun addAssetsImages(mContext: Context, folderPath: String): ArrayList<String>? {
        val pathList: ArrayList<String> = ArrayList()
        try {
            val files: Array<String> = mContext.getAssets().list(folderPath) as Array<String>
            for (name in files) {
                pathList.add(folderPath + File.separator.toString() + name)
                Log.e("pathList item", folderPath + File.separator.toString() + name)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return pathList
    }

}



