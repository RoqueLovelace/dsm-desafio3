package com.rodriguezcortez.desafio.network

import com.rodriguezcortez.desafio.model.Resource
import com.rodriguezcortez.desafio.model.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("resources")
    fun getResources(): Call<List<Resource>>
}