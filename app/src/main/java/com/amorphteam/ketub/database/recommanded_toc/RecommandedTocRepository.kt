package com.amorphteam.ketub.database.recommanded_toc

import com.amorphteam.ketub.model.RecommandedTocModel
import com.amorphteam.ketub.model.ReferenceModel

class RecommandedTocRepository(private val recommandedTocDao: RecommandedTocDao) {
    fun getAllReferences(): List<RecommandedTocModel> = recommandedTocDao.getAllToc()

}