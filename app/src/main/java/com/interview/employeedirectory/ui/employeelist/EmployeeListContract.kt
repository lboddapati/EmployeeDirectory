package com.interview.employeedirectory.ui.employeelist

import com.interview.employeedirectory.base.BasePresenter
import com.interview.employeedirectory.models.Employee

interface EmployeeListContract {

    interface View {
        fun displayLoading()
        fun displayError()
        fun displayEmptyState()
        fun displayEmployeeList(employees: List<Employee>)
    }

    interface Presenter: BasePresenter {
        fun onRetryClicked()
    }
}