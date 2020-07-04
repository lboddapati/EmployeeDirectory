package com.interview.employeedirectory.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "Employees")
data class Employee(
    @PrimaryKey @field:Json(name = "uuid") val id: String,
    @field:Json(name = "full_name") val fullName: String,
    @field:Json(name = "phone_number") val phoneNumber: String?,
    @field:Json(name = "email_address") val email: String,
    @field:Json(name = "biography") val biography: String?,
    @field:Json(name = "photo_url_small") val smallImageUrl: String?,
    @field:Json(name = "photo_url_large") val largeImageUrl: String?,
    @field:Json(name = "team") val team: String,
    @field:Json(name = "employee_type") val employeeType: EmployeeType
)

enum class EmployeeType {
    FULL_TIME,
    PART_TIME,
    CONTRACTOR
}