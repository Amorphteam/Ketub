package com.amorphteam.ketub.api

import com.amorphteam.ketub.model.ReferenceModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RequestApi {
    @GET("most_read.json")
    fun getMostReadToc(): Deferred<List<ReferenceModel>>

    @GET("recommended.json")
    fun getRecommendedToc(): Deferred<List<ReferenceModel>>
}