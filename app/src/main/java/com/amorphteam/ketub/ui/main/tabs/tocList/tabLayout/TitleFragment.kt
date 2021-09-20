package com.amorphteam.ketub.ui.main.tabs.tocList.tabLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    private lateinit var viewModel: TitleFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        viewModel = ViewModelProvider(this).get(TitleFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return binding.root

    }


}