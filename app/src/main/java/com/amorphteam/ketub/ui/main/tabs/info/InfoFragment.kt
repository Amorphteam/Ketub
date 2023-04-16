package com.amorphteam.nososejtehad.info

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[InfoViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentInfoBinding>(
            inflater,
            R.layout.fragment_info,
            container,
            false
        )

        return binding.root
    }


}