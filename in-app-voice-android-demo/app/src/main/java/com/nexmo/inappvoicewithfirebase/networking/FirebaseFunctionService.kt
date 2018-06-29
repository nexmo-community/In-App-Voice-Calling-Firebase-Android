package com.nexmo.inappvoicewithfirebase.networking

import retrofit2.Call
import retrofit2.http.GET

interface FirebaseFunctionService {
    @GET("jwt")
    fun getJWT(): Call<UserJWT>
}