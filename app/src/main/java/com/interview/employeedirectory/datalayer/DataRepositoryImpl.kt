package com.interview.employeedirectory.datalayer

import android.content.SharedPreferences
import com.interview.employeedirectory.datalayer.memorycache.MemoryCacheRepository
import com.interview.employeedirectory.datalayer.persistentcache.PersistentCacheRepository
import com.interview.employeedirectory.datalayer.persistentcache.asEntity
import com.interview.employeedirectory.datalayer.persistentcache.toDataModel
import com.interview.employeedirectory.models.Employee
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Error

private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"
private const val FAVORITES_KEY = "favorites_key"

class DataRepositoryImpl: DataRepository, KoinComponent {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val api = retrofit.create(EmployeeApi::class.java)

    private val memoryCacheRepository by inject<MemoryCacheRepository>()
    private val persistentCacheRepository by inject<PersistentCacheRepository>()
    private val sharedPreferences by inject<SharedPreferences>()

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

    override fun getFavorites(): Single<Set<String>>
            = Single.just(sharedPreferences.getStringSet(FAVORITES_KEY, setOf()))

    override fun addFavorite(employeeId: String) = Completable.create { emitter ->
        val favorites = sharedPreferences.getStringSet(FAVORITES_KEY, setOf()) ?: setOf()
        val updatedFavorites = favorites.toMutableSet().apply {
            add(employeeId)
        }
        if(sharedPreferences.edit().putStringSet(FAVORITES_KEY, updatedFavorites).commit()) {
            emitter.onComplete()
        } else {
            emitter.onError(Error("Error adding $employeeId to Favorites."))
        }
    }

    override fun removeFavorite(employeeId: String) = Completable.create { emitter ->
        val favorites = sharedPreferences.getStringSet(FAVORITES_KEY, setOf()) ?: setOf()
        val updatedFavorites = favorites.toMutableSet().apply {
            remove(employeeId)
        }
        if(sharedPreferences.edit().putStringSet(FAVORITES_KEY, updatedFavorites).commit()) {
            emitter.onComplete()
        } else {
            emitter.onError(Error("Error removing $employeeId from Favorites."))
        }
    }
}