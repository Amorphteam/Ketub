package com.amorphteam.ketub.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentLibraryBinding
import com.amorphteam.ketub.ui.epub.EpubViewer


class LibraryFragment : Fragment() {

    private lateinit var viewModel: LibraryFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentLibraryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_library, container, false
        )

        viewModel = ViewModelProvider(this).get(LibraryFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startEpubAct.observe(viewLifecycleOwner, {
            if (it) startActivity(Intent(activity, EpubViewer::class.java))
        })
        // Inflate the layout for this fragment
        return binding.root
    }


}