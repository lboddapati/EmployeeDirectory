package com.interview.employeedirectory.datalayer.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM Employees WHERE :currentTime - timeCreated <= :timeToLive")
    fun getEmployees(
        currentTime: Long = System.currentTimeMillis(),
        timeToLive: Long = EmployeeEntity.timeToLive
    ): Maybe<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(employees: List<EmployeeEntity>)

    @Query("DELETE FROM Employees WHERE :currentTime - timeCreated > :timeToLive")
    fun deleteExpired(
        currentTime: Long = System.currentTimeMillis(),
        timeToLive: Long = EmployeeEntity.timeToLive
    ): Completable
}