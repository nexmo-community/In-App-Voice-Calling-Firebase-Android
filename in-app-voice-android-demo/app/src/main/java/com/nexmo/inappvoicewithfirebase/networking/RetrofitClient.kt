package com.nexmo.inappvoicewithfirebase.networking

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


var retrofitClient = Retrofit.Builder()
        .baseUrl("https://us-central1-fir-stitch-android.cloudfunctions.net/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

var retrofitService = retrofitClient.create<FirebaseFunctionService>(FirebaseFunctionService::class.java)