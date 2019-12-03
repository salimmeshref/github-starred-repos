package com.salim.maticcodingchallenge.model

import com.google.gson.annotations.SerializedName

//defines owner  fields
data class Owner(
        @SerializedName("login")
        val username:String?,
        @SerializedName("avatar_url")
        val avatarUrl:String?
)

//defines repo fields
data class Repo(
        @SerializedName("owner")
        val owner:Owner?,
        @SerializedName("name")
        val name:String?,
        @SerializedName("description")
        val description:String?,
        @SerializedName("stargazers_count")
        val starsCount:Int
)

//defines response  fields
data class Response(
        @SerializedName("items")
        val repos:List<Repo>
)

