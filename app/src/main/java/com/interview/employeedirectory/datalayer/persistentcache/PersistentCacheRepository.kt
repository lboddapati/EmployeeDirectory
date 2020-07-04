package com.interview.employeedirectory.datalayer.persistentcache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.interview.employeedirectory.base.SubscriptionConfig
import org.koin.core.KoinComponent
import org.koin.core.inject

@Database(entities = [EmployeeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PersistentCacheRepository: RoomDatabase(), KoinComponent {
    abstract val employeeDao: EmployeeDao

    private val subscriptionConfig: SubscriptionConfig by inject()

    fun clearExpiredItems() {
        employeeDao.deleteExpired()
            .subscribeOn(subscriptionConfig.subscribeOnScheduler)
            .observeOn(subscriptionConfig.observeOnScheduler)
            .subscribe()
    }
}