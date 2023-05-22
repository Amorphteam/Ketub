package com.amorphteam.ketub.ui.main.tabs.library

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amorphteam.ketub.R
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.model.BookModel
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.model.CatSection
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.DatabaseBookHelper
import com.amorphteam.ketub.utility.FileManager
import com.amorphteam.ketub.utility.Keys
import com.bumptech.glide.Glide

class DetailViewModel(private val bookRepository: BookRepository, var catSection: CatSection) :
    ViewModel() {
    private val _startEpubAct = MutableLiveData<Boolean>()
    val startEpubAct: LiveData<Boolean>
        get() = _startEpubAct

    private val _startLibraryFrag = MutableLiveData<Boolean>()
    val startLibraryFrag: LiveData<Boolean>
        get() = _startLibraryFrag

    private var _allCats = MutableLiveData<List<CategoryModel>>()
    val allCats: LiveData<List<CategoryModel>?>
        get() = _allCats

    private var _bookItems = MutableLiveData<List<BookModel>>()
    val bookItems: LiveData<List<BookModel>>
        get() = _bookItems

    private var databaseBookHelper: DatabaseBookHelper? = DatabaseBookHelper.getInstance()

    init {
        databaseBookHelper?.getCats(catSection.title, bookRepository, _allCats)
    }

    fun openLibraryFrag() {
        _startLibraryFrag.value = true
    }

    override fun onCleared() {
        super.onCleared()
        databaseBookHelper = null
    }



    fun getCatId(id: Int) {
        databaseBookHelper?.getBookItems(id, bookRepository, _bookItems)
    }

    companion object {

        @SuppressLint("DiscouragedApi")
        @JvmStatic
        @BindingAdapter("setLogoImage")
        fun setImage(image: ImageView, drawableImage: String){
            Glide.with(image.context)
                .load(image.context.resources
                    .getIdentifier(drawableImage, "drawable", image.context.packageName))
                .placeholder(R.drawable.ejtihad_logo)
                .into(image)
        }

    }
}