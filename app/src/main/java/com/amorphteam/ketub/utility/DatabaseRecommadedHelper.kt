package com.amorphteam.ketub.utility

import androidx.lifecycle.MutableLiveData
import com.amorphteam.ketub.database.recommanded_toc.RecommandedTocRepository
import com.amorphteam.ketub.database.reference.ReferenceRepository
import com.amorphteam.ketub.model.RecommandedTocModel
import com.amorphteam.ketub.model.ReferenceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseRecommadedHelper {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getOfflineRecommandedToc(
        recommandedTocRepository: RecommandedTocRepository,
        arrayList: MutableLiveData<List<RecommandedTocModel>>
    ) {
        uiScope.launch {
            arrayList.value = getAllToc(recommandedTocRepository)
        }
    }

    suspend fun getAllToc(recommandedTocRepository: RecommandedTocRepository): List<RecommandedTocModel> {
        return withContext(Dispatchers.IO) {
            val tocs = recommandedTocRepository.getAllReferences()
            tocs
        }
    }

    companion object {
        private var instance: DatabaseRecommadedHelper? = null

        fun getInstance(): DatabaseRecommadedHelper {
            if (instance == null) {
                instance = DatabaseRecommadedHelper()
            }
            return instance!!
        }
    }
}

