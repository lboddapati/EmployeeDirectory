package com.interview.employeedirectory.ui.employeelist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.interview.employeedirectory.base.LifecycleAwareSubscriptionManager
import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.models.Employee
import io.reactivex.observers.DisposableSingleObserver
import org.koin.core.KoinComponent
import org.koin.core.inject

class EmployeeListPresenter(
    private val view: EmployeeListContract.View,
    private val subscriptionManager: LifecycleAwareSubscriptionManager
): EmployeeListContract.Presenter, KoinComponent {

    private val dataRepository : DataRepository by inject()

    override fun onRetryClicked() = loadEmployeeList()

    override fun onEmployeeSelected(employee: Employee) {
        view.openEmployeeDetail(employee)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun loadEmployeeList() {
        view.displayLoading()
        subscriptionManager.subscribe(
            dataRepository.getEmployees(),
            object: DisposableSingleObserver<List<Employee>>() {
                override fun onSuccess(employees: List<Employee>) {
                    if (employees.isEmpty()) {
                        view.displayEmptyState()
                    } else {
                        view.displayEmployeeList(employees)
                    }
                }

                override fun onError(error: Throwable) {
                    view.displayError()
                }
            }
        )
    }
}