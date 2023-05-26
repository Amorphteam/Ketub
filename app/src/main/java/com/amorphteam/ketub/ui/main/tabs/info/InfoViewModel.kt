package com.amorphteam.nososejtehad.info

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.utility.Keys

class InfoViewModel : ViewModel() {
    fun openWebsite(view: View){
        val url = "https://nosos.net"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }

    fun openPrivacyPage(view: View){
        val url = "https://amorphteam.com/PrivacyPolicy.html"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        view.context.startActivity(intent)
    }

    fun sendMail(view: View){
        val recipient = "info@nosos.net"
        val subject = " من تطبيق نصوص معاصرة والاجتهاد والتجديد"
        val body = "Your email body"

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        view.context.startActivity(intent)
    }

}