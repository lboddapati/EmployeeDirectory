package com.interview.employeedirectory.datalayer

import android.util.Log
import com.interview.employeedirectory.datalayer.cache.PersistentCache
import com.interview.employeedirectory.datalayer.cache.asEntity
import com.interview.employeedirectory.datalayer.cache.toDataModel
import com.interview.employeedirectory.models.Employee
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"

class DataRepositoryImpl: DataRepository, KoinComponent {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val api = retrofit.create(EmployeeApi::class.java)

    private val persistentCache: PersistentCache by inject()

    override fun getEmployees(): Single<List<Employee>> {
        return persistentCache.employeeDao.getEmployees()
            .map { it.toDataModel() }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(
                api.getEmployees()
                    .map { it.employees }
                    .doOnSuccess { persistentCache.employeeDao.insertAll(it.asEntity()) }
            )
    }
}