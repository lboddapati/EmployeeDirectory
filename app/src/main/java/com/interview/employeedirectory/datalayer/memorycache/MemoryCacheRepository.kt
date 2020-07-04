package com.interview.employeedirectory.datalayer.memorycache

import com.interview.employeedirectory.models.Employee
import io.reactivex.Maybe

class MemoryCacheRepository {
    private val employeeCache = MemoryCache<List<Employee>>()

    fun clearExpiredItems() {
        employeeCache.removeExpired()
    }

    fun getEmployees(): Maybe<List<Employee>> = employeeCache.get().filter { it.isNotEmpty() }

    fun insertEmployees(employees: List<Employee>) {
        employeeCache.add(employees)
    }
}