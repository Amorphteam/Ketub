package com.amorphteam.ketub.ui.main.tabs.library.api

import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import io.reactivex.Flowable
import retrofit2.http.GET

interface RequestApi {
    @GET("most_read.json")
    fun getMostReadToc(): Flowable<ArrayList<MainToc>>

    @GET("recommanded.json")
    fun getRecommandedToc(): Flowable<ArrayList<MainToc>>
}