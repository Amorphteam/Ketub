package com.amorphteam.ketub.ui.epub.fragments.toc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.amorphteam.ketub.R

class TocSingleFragment : Fragment() {
    private lateinit var binding: com.amorphteam.ketub.databinding.FragmentTocSingleBinding
    private lateinit var viewModel: TocSingleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[TocSingleViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_toc_single, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubFrag.observe(viewLifecycleOwner) {
//            if (it) startActivity(Intent(activity, EpubActivity::class.java))
        }
//        val adapter = IndexExpandableAdapter()
//        adapter.submitList(viewModel.tocGroupItems.value!!)
//        binding.expandableListView.setAdapter(adapter)

//        adapter.clickListener.setOnGroupClickListener {
//            viewModel.openEpubFrag()
//        }
        binding.searchbar.back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
        }
//        handleSearchView(binding.searchbar.searchView, adapter)

        return binding.root
    }


    private fun handleSearchView(
        searchView: androidx.appcompat.widget.SearchView,
    ) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                filterSearch(newText, index)

                return true
            }
        })

    }




}