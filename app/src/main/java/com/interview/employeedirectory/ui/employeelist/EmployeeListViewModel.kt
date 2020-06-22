package com.interview.employeedirectory.ui.employeelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.interview.employeedirectory.models.Employee

private const val SAVED_EMPLOYEES_KEY = "saved_employees"
class EmployeeListViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val employees: ArrayList<Employee> = savedStateHandle[SAVED_EMPLOYEES_KEY] ?: arrayListOf()

    fun saveState() {
        savedStateHandle.set(SAVED_EMPLOYEES_KEY, employees)
    }
}