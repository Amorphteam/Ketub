package com.amorphteam.nososejtehad.info

import android.content.Intent
import android.net.Uri
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

        val binding = DataBindingUtil.inflate<FragmentInfoBinding>(
            inflater,
            R.layout.fragment_info,
            container,
            false
        )
        binding.openWebsite.setOnClickListener {
            openWebsite()
        }
        binding.sendMail.setOnClickListener {
            sendMail()
        }
        binding.privacy.setOnClickListener {
            openPrivacyPage()
        }
        return binding.root
    }
    fun openWebsite() {
        val url = "https://nosos.net"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(intent)
    }

    fun openPrivacyPage() {
        val url = "https://amorphteam.com/PrivacyPolicy.html"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(intent)
    }

    fun sendMail() {
        val recipient = "mdohayni@hotmail.com"
        val subject = " من تطبيق نصوص معاصرة والاجتهاد والتجديد"
        val body = "Your email body"

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        requireContext().startActivity(intent)
    }


}