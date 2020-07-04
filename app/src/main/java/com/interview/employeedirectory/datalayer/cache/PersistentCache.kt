package com.interview.employeedirectory.datalayer.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.interview.employeedirectory.base.SubscriptionConfig
import com.interview.employeedirectory.models.Employee
import org.koin.core.KoinComponent
import org.koin.core.inject

@Database(entities = [Employee::class], version = 1)
@TypeConverters(Converters::class)
abstract class PersistentCache: RoomDatabase(), KoinComponent {
    abstract val employeeDao: EmployeeDao

    private val subscriptionConfig: SubscriptionConfig by inject()

    // TODO: Add expiration (TTL) to cache entities
    fun clearExpiredItems() {
        employeeDao.deleteAll()
            .subscribeOn(subscriptionConfig.subscribeOnScheduler)
            .observeOn(subscriptionConfig.observeOnScheduler)
            .subscribe()
    }
}