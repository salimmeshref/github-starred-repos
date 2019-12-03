package com.salim.maticcodingchallenge.di

import com.salim.maticcodingchallenge.model.ReposApi
import com.salim.maticcodingchallenge.model.ReposService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
//Module and Provides indicates classes and methods that provides injection
@Module
class ApiModule {

    private val baseUrl ="https://api.github.com/"

    //return ReposApi
    @Provides
    fun providesGetReposApi():ReposApi{
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ReposApi::class.java)
    }

    //return ReposService
    @Provides
    fun providesReposService():ReposService{
        return ReposService()
    }

}