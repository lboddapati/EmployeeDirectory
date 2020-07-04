package com.interview.employeedirectory.datalayer

import com.interview.employeedirectory.datalayer.memorycache.MemoryCacheRepository
import com.interview.employeedirectory.datalayer.persistentcache.PersistentCacheRepository
import com.interview.employeedirectory.datalayer.persistentcache.asEntity
import com.interview.employeedirectory.datalayer.persistentcache.toDataModel
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

    private val memoryCacheRepository: MemoryCacheRepository by inject()
    private val persistentCacheRepository: PersistentCacheRepository by inject()

    override fun getEmployees(): Single<List<Employee>> = getEmployeesFromMemoryCache()
        .switchIfEmpty(getEmployeesFromPersistentCache() )
        .switchIfEmpty(getEmployeesFromNetwork())

    private fun getEmployeesFromMemoryCache() = memoryCacheRepository.getEmployees()

    private fun getEmployeesFromPersistentCache() = persistentCacheRepository.employeeDao.getEmployees()
        .map { it.toDataModel() }
        .filter { it.isNotEmpty() }
        .doOnSuccess { memoryCacheRepository.insertEmployees(it) }

    private fun getEmployeesFromNetwork() = api.getEmployees()
        .map { it.employees }
        .doOnSuccess {
            memoryCacheRepository.insertEmployees(it)
            persistentCacheRepository.employeeDao.insertAll(it.asEntity())
        }
}