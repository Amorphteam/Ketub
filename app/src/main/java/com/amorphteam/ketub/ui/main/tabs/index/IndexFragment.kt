package com.amorphteam.nososejtehad.index

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentIndexBinding


class IndexFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProvider(this)[IndexViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentIndexBinding>(
            inflater,
            R.layout.fragment_index,
            container,
            false
        )

        return binding.root
    }


}