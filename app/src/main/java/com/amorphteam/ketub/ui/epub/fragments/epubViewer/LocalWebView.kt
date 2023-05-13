package com.amorphteam.ketub.ui.epub.fragments.epubViewer

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.amorphteam.ketub.utility.AssetUtil
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
        Log.i("AJCssfff", url)
        if (url.contains("Style0001.css")) {
            return AssetUtil.instance?.getCssForName(context, STYLE_OUR)
        }

        if (url.contains("normal.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_NORMAL, "application/x-font-ttf")
        }
        if (url.contains("heading1.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADING1, "application/x-font-ttf")
        }
        if (url.contains("heading2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_HEADING2, "application/x-font-ttf")
        }
        if (url.contains("custom1.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_CUSTOM1, "application/x-font-ttf")
        }
        if (url.contains("custom2.ttf")) {
            return AssetUtil.instance?.getFontFroName(context, FONT_CUSTOM2, "application/x-font-ttf")
        }

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
        const val STYLE_OUR = "css/CustomStyle.css"
        const val FONT_NORMAL = "font/normal.ttf"
        const val FONT_HEADING1 = "font/heading1.ttf"
        const val FONT_HEADING2 = "font/heading2.ttf"
        const val FONT_CUSTOM1 = "font/custom1.ttf"
        const val FONT_CUSTOM2 = "font/custom2.ttf"

        private const val REJAL_LINK = "sahifa#"
        private const val SHORT_LINK = "short#"
    }
}
