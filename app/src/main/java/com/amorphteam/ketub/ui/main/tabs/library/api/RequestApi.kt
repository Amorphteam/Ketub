package com.amorphteam.ketub.ui.main.tabs.library.api

import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RequestApi {
    @GET("most_read.json")
    fun getMostReadToc(): Deferred<List<MainToc>>

    @GET("recommanded.json")
    fun getRecommandedToc(): Deferred<List<MainToc>>
}