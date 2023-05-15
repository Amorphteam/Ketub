package com.amorphteam.ketub.ui.epub

import android.content.Intent
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.Keys.Companion.RQ_SIDELONG
import java.lang.ref.WeakReference

class EpubVerticalDelegate private constructor() {
    private var mRef: WeakReference<EpubActivity>? = null
    private fun set(activity: EpubActivity) {
        mRef = WeakReference<EpubActivity>(activity)
    }

    private fun destroy() {
        if (mRef != null) {
            mRef!!.clear()
        }
    }

    val activity: EpubActivity?
        get() = mRef!!.get()

    /**
     * Open activity that contains toc, bookmark, and highlights
     *
     * @param bookAddress
     */
    fun getToSidelongActivity(bookAddress: String?) {
        val intent: Intent = Intent(
            mRef!!.get(),
            EpubActivity::class.java
        )
        intent.putExtra(Keys.BOOK_ADDRESS, bookAddress)
        mRef!!.get()?.startActivityForResult(intent, RQ_SIDELONG)
    }

    companion object {
        private var sInstance: EpubVerticalDelegate? = null
        fun subscribeOn(activity: EpubActivity) {
            sInstance = EpubVerticalDelegate()
            sInstance!!.set(activity)
        }

        fun get(): EpubVerticalDelegate? {
            return sInstance
        }

        fun unSubscribe(activity: EpubActivity?) {
            if (sInstance != null && activity != null) {
                val refActivity: EpubActivity? = get()!!.mRef!!.get()
                if (activity != refActivity) {
                    return
                }
                sInstance!!.destroy()
                sInstance = null
            }
        }
    }
}
