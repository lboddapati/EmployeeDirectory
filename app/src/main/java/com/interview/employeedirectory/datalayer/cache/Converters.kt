package com.interview.employeedirectory.datalayer.cache

import androidx.room.TypeConverter
import com.interview.employeedirectory.models.EmployeeType

class Converters {
    @TypeConverter
    fun employeeTypeToString(type: EmployeeType) = type.name

    @TypeConverter
    fun employeeTypeFromString(typeString: String) = EmployeeType.valueOf(typeString)
}