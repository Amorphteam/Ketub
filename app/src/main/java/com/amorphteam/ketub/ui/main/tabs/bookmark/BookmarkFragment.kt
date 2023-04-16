package com.amorphteam.nososejtehad.bookmark

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.FragmentBookmarkBinding


class BookmarkFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[BookmarkViewModel::class.java]
        val binding = DataBindingUtil.inflate<FragmentBookmarkBinding>(inflater, R.layout.fragment_bookmark, container, false)

        return binding.root    }



}