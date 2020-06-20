package com.interview.employeedirectory.datalayer

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

// TODO: Fill in base utl here
private const val BASE_URL = ""

class DataRepositoryImpl: DataRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)
}