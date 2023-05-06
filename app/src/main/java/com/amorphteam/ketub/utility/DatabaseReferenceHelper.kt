package com.amorphteam.ketub.utility

import androidx.lifecycle.MutableLiveData
import com.amorphteam.ketub.api.TocApi
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.ReferenceModel
import kotlinx.coroutines.*

class DatabaseReferenceHelper {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private suspend fun getAllReferences(onlineReference: OnlineReference, referecnceRepository: ReferenceRepository): List<ReferenceModel> {
        return withContext(Dispatchers.IO) {
            val references = referecnceRepository.getAllReferences(onlineReference)
            references
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