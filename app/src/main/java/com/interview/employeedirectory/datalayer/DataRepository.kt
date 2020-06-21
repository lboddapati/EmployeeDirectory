package com.interview.employeedirectory.datalayer

import com.interview.employeedirectory.models.Employee
import io.reactivex.Single

interface DataRepository {
    fun getEmployees(): Single<List<Employee>>
}