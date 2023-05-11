package com.amorphteam.ketub.ui.main.tabs.library

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.*
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.model.CatSection
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.*
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import java.security.Key

open class LibraryViewModel(
    private val bookRepository: BookRepository,
    private val referenceRepository: ReferenceRepository
) : ViewModel() {
    private val _startSearchAct = MutableLiveData<Boolean>()
    val startSearchAct: LiveData<Boolean>
        get() = _startSearchAct

    private val _startDetailFrag = MutableLiveData<CatSection>()
    val startDetailFrag: LiveData<CatSection>
        get() = _startDetailFrag

    private val _readMoreToc = MutableLiveData<List<ReferenceModel>>()
    val readMoreToc: LiveData<List<ReferenceModel>>
        get() = _readMoreToc

    private val _recommendedToc = MutableLiveData<List<ReferenceModel>>()
    val recommendedToc: LiveData<List<ReferenceModel>>
        get() = _recommendedToc

    private val _errorTocRecieve = MutableLiveData<String>()
    val errorTocRecieve: LiveData<String>
        get() = _errorTocRecieve

    private var databaseBookHelper: DatabaseBookHelper? = DatabaseBookHelper.getInstance()
    private var databaseReferenceHelper: DatabaseReferenceHelper? = DatabaseReferenceHelper.getInstance()



    private var _firstCatBooksNewItems = MutableLiveData<List<CategoryModel>>()
    val firstCatBooksNewItems: LiveData<List<CategoryModel>>
        get() = _firstCatBooksNewItems

    private var _secondCatBooksNewItems = MutableLiveData<List<CategoryModel>>()
    val secondCatBooksNewItems: LiveData<List<CategoryModel>>
        get() = _secondCatBooksNewItems


    private val _startEpubAct = MutableLiveData<String>()
    val startEpubAct: LiveData<String>
        get() = _startEpubAct



    private var _bookItems = MutableLiveData<List<BookModel>>()
    val bookItems: LiveData<List<BookModel>>
        get() = _bookItems


    init {
        getCatSections()
        getReferences()
    }

    private fun getCatSections() {
        databaseBookHelper?.getCats(Keys.DB_FIRST_CAT, bookRepository, _firstCatBooksNewItems)
        databaseBookHelper?.getCats(Keys.DB_SECOND_CAT, bookRepository, _secondCatBooksNewItems)
    }

    private fun getReferences() {
        if (Connection.isInternetConnected()) {
            databaseReferenceHelper?.getOnlineReference(OnlineReference.RECOMMENDED_ONLINE, referenceRepository, _recommendedToc)
            databaseReferenceHelper?.getOnlineReference(OnlineReference.READMORE_ONLINE, referenceRepository, _readMoreToc)
        }
        databaseReferenceHelper?.getOfflineReference(OnlineReference.RECOMMENDED_ONLINE, referenceRepository, _recommendedToc)
        databaseReferenceHelper?.getOfflineReference(OnlineReference.READMORE_ONLINE, referenceRepository, _readMoreToc)

    }


    override fun onCleared() {
        super.onCleared()
        databaseBookHelper = null
        databaseReferenceHelper = null
    }

    fun openEpubAct(bookAddress:String) {
        _startEpubAct.value = bookAddress
    }

    fun getCatId(id: Int) {
        databaseBookHelper?.getBookItems(id, bookRepository, _bookItems)
    }


    fun openDetailFrag(title: CatSection) {
        _startDetailFrag.value = title
    }

    fun openSearchAct() {
        _startSearchAct.value = true
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun setImage(image: ImageView, item: CategoryModel?) {
            val fileManager = FileManager(image.context)
            val imageAddress = item!!.catCover?.let { fileManager.getCoverUri(it) }
            Glide.with(image.context)
                .load(Uri.parse(imageAddress.toString()))
                .placeholder(R.drawable.ejtihad_logo)
                .into(image)
        }

        @SuppressLint("DiscouragedApi")
        @JvmStatic
        @BindingAdapter("setImage")
        fun setImage(image: ImageView, item: ReferenceModel){
            var drawableImage = "ejtihad_logo"
            if (item.bookName.contains(Keys.DB_SECOND_CAT)){
                drawableImage = "nosos_logo"
            }
            Glide.with(image.context)
                .load(image.context.resources
                    .getIdentifier(drawableImage, "drawable", image.context.packageName))
                .placeholder(R.drawable.ejtihad_logo)
                .into(image)
        }

    }


}
