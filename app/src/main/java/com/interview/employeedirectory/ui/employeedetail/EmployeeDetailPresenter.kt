package com.interview.employeedirectory.ui.employeedetail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.interview.employeedirectory.models.Employee

class EmployeeDetailPresenter(
    private val employee: Employee?,
    private val view: EmployeeDetailContract.View
): EmployeeDetailContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        if (employee == null) {
            view.displayError()
        } else {
            view.displayEmployee(employee)
        }
    }

    override fun onPhoneClicked() {
        employee?.phoneNumber?.let { view.startPhoneCall(it) }
    }

    override fun onEmailClicked() {
        employee?.email?.let { view.startEmail(it) }
    }
}