package com.interview.employeedirectory.ui.employeelist

import android.os.Bundle
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
    private val viewModel: EmployeeListViewModel,
    private val subscriptionManager: LifecycleAwareSubscriptionManager
): EmployeeListContract.Presenter, KoinComponent {

    private val dataRepository : DataRepository by inject()

    override fun onRetryClicked() = loadEmployeeList()

    override fun onSaveInstanceState(savedState: Bundle) = viewModel.saveState(savedState)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        if (viewModel.employees.isNotEmpty()) {
            view.displayEmployeeList(viewModel.employees)
            return
        }
        loadEmployeeList()
    }

    private fun loadEmployeeList() {
        viewModel.employees.clear()
        view.displayLoading()
        subscriptionManager.subscribe(
            dataRepository.getEmployees(),
            object: DisposableSingleObserver<List<Employee>>() {
                override fun onSuccess(employees: List<Employee>) {
                    if (employees.isEmpty()) {
                        view.displayEmptyState()
                    } else {
                        view.displayEmployeeList(employees)
                        viewModel.employees.addAll(employees)
                    }
                }

                override fun onError(error: Throwable) {
                    view.displayError()
                }
            }
        )
    }
}