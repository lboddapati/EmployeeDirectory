package com.interview.employeedirectory.ui.employeelist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.interview.employeedirectory.base.testApplicationModule
import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.models.Employee
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get
import org.koin.test.inject

class EmployeeListPresenterTest: KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(
        testApplicationModule,
        module {
            single<EmployeeListContract.View> { mock() }
        }
    )}

    private val dataRepository: DataRepository by inject()
    private val view: EmployeeListContract.View by inject()

    @Test
    fun onCreate_withoutSavedState_loadsEmployees() {
        whenever(dataRepository.getEmployees()).thenReturn(Single.just(listOf()))
        LifecycleRegistry(mock()).apply {
            addObserver(EmployeeListPresenter(view, EmployeeListViewModel(), get()))
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        verify(dataRepository).getEmployees()
    }

    fun onCreate_withSavedState_displaysSavedResults() {
        val viewModel = EmployeeListViewModel(arrayListOf(mock<Employee>()))
        LifecycleRegistry(mock()).apply {
            addObserver(EmployeeListPresenter(view, viewModel, get()))
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        verify(view).displayEmployeeList(viewModel.employees)
        verify(dataRepository, never()).getEmployees()
    }

    @Test
    fun onRetryClicked_loadsEmployees() {
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.just(listOf()))
        EmployeeListPresenter(view, EmployeeListViewModel(), get()).onRetryClicked()

        verify(dataRepository).getEmployees()
    }

    @Test
    fun onEmployeeListRequestSuccess_emptyResults_updatesViewWithEmptyState() {
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.just(listOf()))
        // Trigger request
        EmployeeListPresenter(view, EmployeeListViewModel(), get()).onRetryClicked()

        verify(view).displayEmptyState()
    }

    @Test
    fun onEmployeeListRequestSuccess_nonEmptyResults_updatesViewAndModelWithResults() {
        val viewModel = EmployeeListViewModel()
        val loadedEmployees = listOf(mock<Employee>())
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.just(loadedEmployees))
        // Trigger request
        EmployeeListPresenter(view, viewModel, get()).onRetryClicked()

        verify(view).displayEmployeeList(loadedEmployees)
        assertTrue(viewModel.employees.containsAll(loadedEmployees))
    }

    @Test
    fun onEmployeeListRequestFailure_updatesViewWithError() {
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.error(Throwable()))
        // Trigger request
        EmployeeListPresenter(view, EmployeeListViewModel(), get()).onRetryClicked()

        verify(view).displayError()
    }

    @Test
    fun onSaveInstanceState_delegatesToViewModel() {
        val viewModel = mock<EmployeeListViewModel>()
        EmployeeListPresenter(view, viewModel, get()).onSaveInstanceState(mock())

        verify(viewModel).saveState(any())
    }
}