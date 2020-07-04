package com.interview.employeedirectory.datalayer.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.interview.employeedirectory.models.Employee
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM Employees")
    fun getEmployees(): Maybe<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(employees: List<Employee>)

    @Query("DELETE FROM Employees")
    fun deleteAll(): Completable
}