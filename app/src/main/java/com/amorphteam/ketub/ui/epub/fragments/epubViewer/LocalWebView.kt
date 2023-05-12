package com.amorphteam.ketub.ui.epub.fragments.epubViewer

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.TextView
import com.amorphteam.ketub.utility.AssetUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mehdok.fineepublib.epubviewer.jsepub.webview.NormalWebView
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*

class LocalWebView : NormalWebView {
    var ctx: Context? = null


    constructor(
        context: Context?,
        ) : super(context) {
        super.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        ctx = context
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        super.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        ctx = context
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        super.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        ctx = context
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        super.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context, attrs, defStyleAttr, privateBrowsing) {
        super.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun onRequest(url: String): WebResourceResponse? {
        if (url.contains("Style0001.css")) {
            return AssetUtil.instance?.getCssForName(context, STYLE_OUR)
        }
        if (url.contains("MehdokStyle22.css")) {
            return AssetUtil.instance?.getCssForName(context, OVERRITED_CSS)
        }
        if (url.contains("Mosawi.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_MOSAWI, "application/x-font-ttf")
        }
        if (url.contains("Vazir.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_VAZIR, "application/x-font-ttf")
        }
        if (url.contains("nazanin.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NAZANIN, "application/x-font-ttf")
        }
        if (url.contains("DroidKufi.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_DROIDKUFI, "application/x-font-ttf")
        }
        //standard fonts
        if (url.contains("NormalAR.otf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NARMALAR, "application/x-font-ttf")
        }
        if (url.contains("NormalAR2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NARMALAR2, "application/x-font-ttf")
        }
        if (url.contains("NormalFA.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NARMALFA, "application/x-font-ttf")
        }
        if (url.contains("NormalFA2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NARMALFA2, "application/x-font-ttf")
        }
        if (url.contains("NormalEN.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NARMALEN, "application/x-font-ttf")
        }
        if (url.contains("NormalEN2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NARMALEN2, "application/x-font-ttf")
        }
        if (url.contains("HeadingAR.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADINGAR, "application/x-font-ttf")
        }
        if (url.contains("HeadingAR2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADINGAR2, "application/x-font-ttf")
        }
        if (url.contains("HeadingFA.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADINGFA, "application/x-font-ttf")
        }
        if (url.contains("HeadingFA2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADINGFA2, "application/x-font-ttf")
        }
        if (url.contains("HeadingEN.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADINGEN, "application/x-font-ttf")
        }
        if (url.contains("HeadingEN2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADINGEN2, "application/x-font-ttf")
        }
        if (url.contains("Symbols.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_SYMBOLS, "application/x-font-ttf")
        }
        if (url.contains("Symbols2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_SYMBOLS2, "application/x-font-ttf")
        }
        if (url.contains("Hadith.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HADITH, "application/x-font-ttf")
        }
        if (url.contains("Hadith2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HADITH2, "application/x-font-ttf")
        }
        if (url.contains("Qoran.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_QORAN, "application/x-font-ttf")
        }
        if (url.contains("Qoran2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_QORAN2, "application/x-font-ttf")
        }
        if (url.contains("Special.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_SPECIAL, "application/x-font-ttf")
        }
        if (url.contains("Special2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_SPECIAL2, "application/x-font-ttf")
        }
        //standard fonts

        // this is internal link, either with hashtag or not, so it must handle via app
        if (url.startsWith("http://localhost") && (url.contains("xhtml") || url.contains("html"))) {
//            EpubVerticalDelegate.get().getActivity().onInternalLickClicked(correctUrlOpf(url))

            // returning empty response or NOT ??
            return WebResourceResponse("", "UTF-8", null)
        }
        return super.onRequest(url)
    }

    override fun onPageFinished() {}
    override fun onPageStarted() {}
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.contains(REJAL_LINK)) {
            var path = url.substring(url.lastIndexOf('/') + 1)
            path = path.replace(REJAL_LINK, "")
            rejalLinkClicked(path)
            return true
        } else if (url.contains(SHORT_LINK)) {
            var shortTafseerString = url.substring(url.lastIndexOf('/'))
            shortTafseerString = shortTafseerString.replace(SHORT_LINK, "")
            shortRejalLinkClicked(shortTafseerString)
            return true
        }
        return false
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (request.url.toString().contains(REJAL_LINK)) {
            val url = request.url.toString()
            var path = url.substring(url.lastIndexOf('/') + 1)
            path = path.replace(REJAL_LINK, "")
            path = (path.toInt() + 1).toString()
            val list: MutableList<String> = ArrayList()
            list.add(path)
            getRejalLinks(list)
            return true
        } else if (request.url.toString().contains(SHORT_LINK)) {
            val url = request.url.toString()
            var shortTafseerString = url.substring(url.lastIndexOf('/') + 1)
            shortTafseerString = shortTafseerString.replace(SHORT_LINK, "")
            var Utf8String: String? = ""
            try {
                Utf8String = URLDecoder.decode(shortTafseerString, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            shortRejalLinkClicked(Utf8String)
            return true
        }
        return false
    }

    fun rejalLinkClicked(path: String) {
//        if (databaseLinkListener != null) {
//            val tags = Arrays.asList(*path.split(",".toRegex()).dropLastWhile { it.isEmpty() }
//                .toTypedArray())
//            databaseLinkListener.onDatabaseLinkClicked(tags)
//        }
    }

    fun shortRejalLinkClicked(string: String?) {
//        val customDialog = Dialog(context, R.style.ShortTafseerDialog)
//        customDialog.setContentView(R.layout.custom_dialog_short_link)
//        val mesaage: TextViewNormal = customDialog.findViewById(R.id.message_dialog)
//        mesaage.setText(string)
//        customDialog.show()
    }

    private fun selectSoundOrTafseer(path: String) {
//        val inflater = ctx!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val dialogView: View = inflater.inflate(R.layout.custom_dialog, null)
//        dialog = BottomSheetDialog(ctx!!)
//        dialog!!.setContentView(dialogView)
//        val textDb = dialog!!.findViewById<TextView>(R.id.text_db)
//        textDb!!.text = path
//        dialogListener.onShowDialogClicked()
//        dialog!!.show()

//        CustomDialogClass cdd=new CustomDialogClass((Activity) getContext(), path, this);
//        cdd.show();
    }

    fun getRejalLinks(tags: List<String>?) {
//        val subscriptions = CompositeDisposable()
//        subscriptions.add(
//            DataRepositoryImpl.getInstance().getRejalLinks(tags)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSubscriber<List<RejalLink?>?>() {
//                    fun onComplete() {}
//                    fun onError(e: Throwable) {
//                        Log.i("aaaa", e.message!!)
//                    }
//
//                    fun onNext(rejalLinks: List<RejalLink>) {
//                        val linkFromDB: RejalLink = rejalLinks[0]
//                        selectSoundOrTafseer(linkFromDB.getDet())
//                    }
//                })
//        )
    }

    fun showSheet(path: String?) {
//        soundListener.onSoundClicked(path)
    }

    companion object {
        const val STYLE_OUR = "css/Style0001.css"
        const val OVERRITED_CSS = "css/MehdokStyle22.css"
        const val FONT_MOSAWI = "font/Mosawi.ttf"
        const val FONT_VAZIR = "font/Vazir.ttf"
        const val FONT_NAZANIN = "font/nazanin.ttf"
        const val FONT_DROIDKUFI = "font/DroidKufi.ttf"

        //    Standard fonts
        const val FONT_NARMALAR = "font/NormalAR.otf"
        const val FONT_NARMALAR2 = "font/NormalAR2.ttf"
        const val FONT_NARMALFA = "font/NormalFA.ttf"
        const val FONT_NARMALFA2 = "font/NormalFA2.ttf"
        const val FONT_NARMALEN = "font/NormalEN.ttf"
        const val FONT_NARMALEN2 = "font/NormalEN2.ttf"
        const val FONT_HEADINGAR = "font/HeadingAR.ttf"
        const val FONT_HEADINGAR2 = "font/HeadingAR2.ttf"
        const val FONT_HEADINGFA = "font/HeadingFA.ttf"
        const val FONT_HEADINGFA2 = "font/HeadingAR2.ttf"
        const val FONT_HEADINGEN = "font/HeadingEN.ttf"
        const val FONT_HEADINGEN2 = "font/HeadingEN2.ttf"
        const val FONT_SYMBOLS = "font/Symbols.ttf"
        const val FONT_SYMBOLS2 = "font/Symbols2.ttf"
        const val FONT_HADITH = "font/Hadith.ttf"
        const val FONT_HADITH2 = "font/Hadith2.ttf"
        const val FONT_QORAN = "font/Qoran.ttf"
        const val FONT_QORAN2 = "font/Qoran2.ttf"
        const val FONT_SPECIAL = "font/Special.ttf"
        const val FONT_SPECIAL2 = "font/Special2.ttf"
        private const val REJAL_LINK = "sahifa#"
        private const val SHORT_LINK = "short#"
    }
}
