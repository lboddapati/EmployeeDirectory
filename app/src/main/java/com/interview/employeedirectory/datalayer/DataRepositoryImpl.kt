package com.interview.employeedirectory.datalayer

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"

class DataRepositoryImpl: DataRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val api = retrofit.create(EmployeeApi::class.java)

    //TODO: Implement Caching
    override fun getEmployees() = api.getEmployees().map { it.employees }
}