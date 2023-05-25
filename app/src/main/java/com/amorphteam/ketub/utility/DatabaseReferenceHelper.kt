package com.amorphteam.ketub.utility

import androidx.lifecycle.MutableLiveData
import com.amorphteam.ketub.api.TocApi
import com.amorphteam.ketub.database.book.BookRepository
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.CategoryModel
import com.amorphteam.ketub.model.ReferenceModel
import kotlinx.coroutines.*

class DatabaseReferenceHelper {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertBookmark(referecnceRepository: ReferenceRepository, referenceModel: ReferenceModel) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                referecnceRepository.insert(referenceModel)
            }
        }
    }

    fun deleteBookmark(id: Int, referecnceRepository: ReferenceRepository) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                referecnceRepository.delete(id)
            }
        }
    }

    suspend fun getAllBookmarks(catName: String, referenceRepository: ReferenceRepository): List<ReferenceModel> {
        return withContext(Dispatchers.IO) {
            val bookmark = referenceRepository.getAllReferences(catName)
            bookmark
        }
    }

    suspend fun getAllBookmarksSingleBook(bookName: String, referenceRepository: ReferenceRepository): List<ReferenceModel> {
        return withContext(Dispatchers.IO) {
            val bookmark = referenceRepository.getAllReferencesForSingleBook(bookName)
            bookmark
        }
    }

    private suspend fun getAllReferences(onlineReference: OnlineReference, referenceRepository: ReferenceRepository): List<ReferenceModel> {
        return withContext(Dispatchers.IO) {
            val references = referenceRepository.getAllReferences(onlineReference)
            references
        }
    }


    private suspend fun getBookmarkSelected(bookName: String, navIndex: Int, referenceRepository: ReferenceRepository): Boolean {
        return withContext(Dispatchers.IO) {
            val references = referenceRepository.getBookmarkSelected(bookName,navIndex)
            references
        }
    }

    fun getSelected(bookName: String, navIndex: Int,referenceRepository: ReferenceRepository, item: MutableLiveData<Boolean>){
        uiScope.launch {
             item.value = getBookmarkSelected(bookName, navIndex, referenceRepository)

        }
    }

    private suspend fun insertToDatabase(
        listResult: List<ReferenceModel>,
        onlineReference: OnlineReference, referenceRepository: ReferenceRepository
    ) {
        withContext(Dispatchers.IO) {
            for (item in listResult) {
                item.onlineReference = onlineReference
                referenceRepository.insert(item)
            }
        }
    }

    fun getOfflineReference(refrenceRepository: ReferenceRepository, arrayList: MutableLiveData<List<ReferenceModel>>, catName: String){
        uiScope.launch {
            arrayList.value = getAllBookmarks(catName, refrenceRepository)
        }
    }


    fun getOfflineReferenceSingleBook(refrenceRepository: ReferenceRepository, arrayList: MutableLiveData<List<ReferenceModel>>, singleBook: String){
        uiScope.launch {
            arrayList.value = getAllBookmarksSingleBook(singleBook, refrenceRepository)
        }
    }
    fun getOfflineReference(onlineReference: OnlineReference, referenceRepository: ReferenceRepository, arrayList: MutableLiveData<List<ReferenceModel>>) {
        uiScope.launch {
            arrayList.value = getAllReferences(onlineReference, referenceRepository)
        }
    }

    fun getOnlineReference(onlineReference: OnlineReference, referenceRepository: ReferenceRepository, arrayList: MutableLiveData<List<ReferenceModel>>) {
        var deferredList = TocApi.retrofitService.getMostReadToc()
        if (onlineReference == OnlineReference.RECOMMENDED_ONLINE){
            deferredList = TocApi.retrofitService.getRecommendedToc()
        }

        uiScope.launch {
            try {
                val listResult = deferredList.await()
                insertToDatabase(listResult, onlineReference, referenceRepository)
            } catch (e: java.lang.Exception) {
                getOfflineReference(onlineReference, referenceRepository, arrayList)
            }
        }

    }


    companion object {
        private var instance: DatabaseReferenceHelper? = null

        fun getInstance(): DatabaseReferenceHelper {
            if (instance == null) {
                instance = DatabaseReferenceHelper()
            }
            return instance!!
        }
    }

}