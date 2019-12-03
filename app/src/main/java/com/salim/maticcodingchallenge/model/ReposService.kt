package com.salim.maticcodingchallenge.model

import com.salim.maticcodingchallenge.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class ReposService{

    //define ReposApi instance as injection
    //lateinit var to assure that the variable will be initialized before use
    @Inject
    lateinit var reposApi:ReposApi

    init {
        //inject reposSerbvice
        DaggerApiComponent.create().inject(this)
    }

    //calls api to get the data form the backend
    fun getRepos(date:String,sort:String, order:String, page:Int):Single<Response>{
        return reposApi.getRepos(date,sort,order, page)
    }
}