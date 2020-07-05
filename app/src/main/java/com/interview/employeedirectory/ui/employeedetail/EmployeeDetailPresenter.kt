package com.interview.employeedirectory.ui.employeedetail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.interview.employeedirectory.base.LifecycleAwareSubscriptionManager
import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.models.Employee
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import org.koin.core.KoinComponent
import org.koin.core.inject

class EmployeeDetailPresenter(
    private val employee: Employee?,
    private val view: EmployeeDetailContract.View,
    private val subscriptionManager: LifecycleAwareSubscriptionManager
): EmployeeDetailContract.Presenter, KoinComponent {

    private val dataRepository by inject<DataRepository>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        if (employee == null) {
            view.displayError()
        } else {
            view.displayEmployee(employee)
            getFavoriteState()
        }
    }

    override fun onPhoneClicked() {
        employee?.phoneNumber?.let { view.startPhoneCall(it) }
    }

    override fun onEmailClicked() {
        employee?.email?.let { view.startEmail(it) }
    }

    override fun onAddFavoriteClicked() {
        val employeeId = employee?.id
        if (employeeId != null) {
            subscriptionManager.subscribe(
                dataRepository.addFavorite(employeeId),
                object: DisposableCompletableObserver() {
                    override fun onComplete() {
                        view.updateFavoriteState(true)
                    }

                    override fun onError(e: Throwable) {
                        // TODO
                        e.printStackTrace()
                    }
                }
            )
        }
    }

    override fun onRemoveFavoriteClicked() {
        val employeeId = employee?.id
        if (employeeId != null) {
            subscriptionManager.subscribe(
                dataRepository.removeFavorite(employeeId),
                object: DisposableCompletableObserver() {
                    override fun onComplete() {
                        view.updateFavoriteState(false)
                    }

                    override fun onError(e: Throwable) {
                        // TODO
                        e.printStackTrace()
                    }
                }
            )
        }
    }

    private fun getFavoriteState() {
        subscriptionManager.subscribe(
            dataRepository.getFavorites(),
            object: DisposableSingleObserver<Set<String>>() {
                override fun onSuccess(favorites: Set<String>) {
                    employee?.id?.let { view.updateFavoriteState(it in favorites) }
                }

                override fun onError(e: Throwable) {
                    // No-op
                    e.printStackTrace()
                }
            }
        )
    }
}