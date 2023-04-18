package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentIndexListFirstBinding
import com.amorphteam.ketub.databinding.FragmentIndexListSecondBinding
import com.amorphteam.ketub.ui.epub.EpubViewer
import com.amorphteam.ketub.ui.main.tabs.index.adapter.IndexClickListener
import com.amorphteam.ketub.ui.main.tabs.index.adapter.RVIndexListAdapter

class IndexListSecondFragment : Fragment() {

    private lateinit var binding: FragmentIndexListSecondBinding
    private lateinit var viewModel: IndexListSecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[IndexListSecondViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_index_list_second, container, false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner) {
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        }

        handleIndexRecyclerView()

        return binding.root
    }

    private fun handleIndexRecyclerView() {

        val index = RVIndexListAdapter(IndexClickListener {
            viewModel.openEpubAct()
        })

        index.submitList(viewModel.getIndexList().value)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = index
    }

}