package com.interview.employeedirectory.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeListResponse(
    @field:Json(name = "employees") val employees: List<Employee>
)