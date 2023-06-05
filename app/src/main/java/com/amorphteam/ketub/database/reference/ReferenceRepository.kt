package com.amorphteam.ketub.database.reference

import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.OnlineReference


class ReferenceRepository(private val referenceDatabaseDao: ReferenceDatabaseDao) {

    fun getAllReferences(): List<ReferenceModel> = referenceDatabaseDao.getAllReferences()

    fun getAllReferences(catName: String): List<ReferenceModel> =
        referenceDatabaseDao.getAllReferences(catName)

    fun getAllReferences(onlineReference: OnlineReference): List<ReferenceModel> =
        referenceDatabaseDao.getAllReferences(onlineReference)

    fun getAllReferencesForSingleBook(bookName: String): List<ReferenceModel> =
        referenceDatabaseDao.getAllReferencesForSingleBook(bookName)

    suspend fun insert(reference: ReferenceModel) =
        referenceDatabaseDao.insert(reference)

    suspend fun delete(id: Int):Int = referenceDatabaseDao.delete(id)

    fun getBookmarkSelected(bookName: String, navIndex: Int): Boolean =
        referenceDatabaseDao.getBookmarkSelected(bookName, navIndex)

}