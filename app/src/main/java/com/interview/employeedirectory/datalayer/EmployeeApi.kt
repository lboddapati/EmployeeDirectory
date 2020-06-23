package com.interview.employeedirectory.datalayer

import com.interview.employeedirectory.models.EmployeeListResponse
import io.reactivex.Single
import retrofit2.http.GET

interface EmployeeApi {

    @GET("employees.json")
    fun getEmployees(): Single<EmployeeListResponse>

    // Uncomment this (and comment out others) to simulate error state
    // @GET("employees_malformed.json")
    // fun getEmployees(): Single<EmployeeListResponse>

    // Uncomment this (and comment out others) to simulate empty state
    // @GET("employees_empty.json")
    // fun getEmployees(): Single<EmployeeListResponse>
}