package com.rodriguezcortez.desafio.network

import com.rodriguezcortez.desafio.model.Resource
import com.rodriguezcortez.desafio.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("resources")
    fun getResources(): Call<List<Resource>>

    @POST("resources")
    fun createResource(@Body resource: Resource): Call<Resource>

    @PUT("resources/{id}")
    fun updateResource(@Path("id") id: String, @Body resource: Resource): Call<Resource>

    @DELETE("resources/{id}")
    fun deleteResource(@Path("id") id: String): Call<Void>
}