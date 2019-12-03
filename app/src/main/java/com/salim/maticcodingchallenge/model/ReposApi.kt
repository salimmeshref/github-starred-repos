package com.salim.maticcodingchallenge.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ReposApi {

    //define the api hit as http get request with its parameters
    @GET("search/repositories")
    fun getRepos(@Query("q") date:String,
        @Query("sort")sort:String,
        @Query("order")order:String,
        @Query("page")page:Int): Single<Response>
}