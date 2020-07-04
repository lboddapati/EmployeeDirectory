package com.interview.employeedirectory.datalayer.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.interview.employeedirectory.models.Employee
import com.interview.employeedirectory.models.EmployeeType

@Entity(tableName = "Employees")
data class EmployeeEntity(
    val timeCreated: Long = System.currentTimeMillis(),
    @PrimaryKey val id: String,
    val fullName: String,
    val phoneNumber: String?,
    val email: String,
    val biography: String?,
    val smallImageUrl: String?,
    val largeImageUrl: String?,
    val team: String,
    val employeeType: EmployeeType
) {
    companion object {
        val timeToLive: Long = java.util.concurrent.TimeUnit.MINUTES.toMillis(1)
    }
}

/** Map EmployeeEntity to Employee model */
fun List<EmployeeEntity>.toDataModel(): List<Employee> {
    return map {
        Employee(
            id = it.id,
            fullName = it.fullName,
            email = it.email,
            phoneNumber = it.phoneNumber,
            biography = it.biography,
            smallImageUrl = it.smallImageUrl,
            largeImageUrl = it.largeImageUrl,
            team = it.team,
            employeeType = it.employeeType
        )
    }
}

/** Map Employee model to EmployeeEntity */
fun List<Employee>.asEntity(): List<EmployeeEntity> {
    return map {
        EmployeeEntity(
            id = it.id,
            fullName = it.fullName,
            email = it.email,
            phoneNumber = it.phoneNumber,
            biography = it.biography,
            smallImageUrl = it.smallImageUrl,
            largeImageUrl = it.largeImageUrl,
            team = it.team,
            employeeType = it.employeeType
        )
    }
}