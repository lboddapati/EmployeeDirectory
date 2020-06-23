package com.interview.employeedirectory.ui.employeelist

import android.os.Bundle
import com.interview.employeedirectory.models.Employee

data class EmployeeListViewModel(
    var employees: ArrayList<Employee> = arrayListOf()
) {
    fun saveState(state: Bundle) {
        state.putParcelableArrayList(SAVED_EMPLOYEES_KEY, employees)
    }

    companion object {
        private const val SAVED_EMPLOYEES_KEY = "saved_employees"

        fun fromBundle(bundle: Bundle) = EmployeeListViewModel(
            bundle.getParcelableArrayList<Employee>(SAVED_EMPLOYEES_KEY) ?: arrayListOf()
        )
    }
}