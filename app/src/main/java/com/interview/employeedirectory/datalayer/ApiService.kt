package com.interview.employeedirectory.datalayer

import com.interview.employeedirectory.models.Employee
import com.interview.employeedirectory.models.EmployeeListResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("employees.json")
    fun getEmployees(): Single<EmployeeListResponse>
}