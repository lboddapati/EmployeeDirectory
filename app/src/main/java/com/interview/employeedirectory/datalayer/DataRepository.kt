package com.interview.employeedirectory.datalayer

import com.interview.employeedirectory.models.Employee
import io.reactivex.Completable
import io.reactivex.Single

interface DataRepository {
    fun getEmployees(): Single<List<Employee>>
    fun getFavorites(): Single<Set<String>>
    fun addFavorite(employeeId: String): Completable
    fun removeFavorite(employeeId: String): Completable
}