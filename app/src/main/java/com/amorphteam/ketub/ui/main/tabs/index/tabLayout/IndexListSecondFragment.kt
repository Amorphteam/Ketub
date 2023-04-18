package com.amorphteam.ketub.ui.main.tabs.index.tabLayout

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentIndexListSecondBinding

class IndexListSecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var viewModel = ViewModelProvider(this)[IndexListSecondViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentIndexListSecondBinding>(
            inflater,
            R.layout.fragment_index_list_second,
            container,
            false
        )

        return binding.root
    }


}