package com.interview.employeedirectory.models

import com.squareup.moshi.Json

data class EmployeeListResponse(
    @field:Json(name = "employees") val employees: List<Employee>
)