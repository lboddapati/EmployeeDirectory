package com.interview.employeedirectory.ui.employeelist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.interview.employeedirectory.base.testApplicationModule
import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.models.Employee
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
            single<EmployeeListViewModel> { mock() }
        }
    )}

    private val dataRepository: DataRepository by inject()
    private val view: EmployeeListContract.View by inject()
    private val viewModel: EmployeeListViewModel by inject()

    @Test
    fun onCreate_withoutSavedState_loadsEmployees() {
        whenever(viewModel.employees).thenReturn(arrayListOf())
        whenever(dataRepository.getEmployees()).thenReturn(Single.just(listOf()))
        LifecycleRegistry(mock()).apply {
            addObserver(EmployeeListPresenter(view, viewModel, get()))
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        verify(dataRepository).getEmployees()
    }

    fun onCreate_withSavedState_displaysSavedResults() {
        val savedEmployees = arrayListOf(mock<Employee>())
        whenever(viewModel.employees).thenReturn(savedEmployees)
        LifecycleRegistry(mock()).apply {
            addObserver(EmployeeListPresenter(view, viewModel, get()))
            handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        verify(view).displayEmployeeList(savedEmployees)
        verify(dataRepository, never()).getEmployees()
    }

    @Test
    fun onRetryClicked_loadsEmployees() {
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.just(listOf()))
        EmployeeListPresenter(view, viewModel, get()).onRetryClicked()

        verify(dataRepository).getEmployees()
    }

    @Test
    fun onEmployeeListRequestSuccess_emptyResults_updatesViewWithEmptyState() {
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.just(listOf()))
        // Trigger request
        EmployeeListPresenter(view, viewModel, get()).onRetryClicked()

        verify(view).displayEmptyState()
    }

    @Test
    fun onEmployeeListRequestSuccess_nonEmptyResults_updatesViewAndModelWithResults() {
        val savedEmployees = arrayListOf<Employee>()
        whenever(viewModel.employees).thenReturn(savedEmployees)
        val loadedEmployees = listOf(mock<Employee>())
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.just(loadedEmployees))
        // Trigger request
        EmployeeListPresenter(view, viewModel, get()).onRetryClicked()

        verify(view).displayEmployeeList(loadedEmployees)
        assertTrue(savedEmployees.containsAll(loadedEmployees))
    }

    @Test
    fun onEmployeeListRequestFailure_updatesViewWithError() {
        whenever(dataRepository.getEmployees())
            .thenReturn(Single.error(Throwable()))
        // Trigger request
        EmployeeListPresenter(view, viewModel, get()).onRetryClicked()

        verify(view).displayError()
    }

    @Test
    fun onSaveInstanceState_delegatesToViewModel() {
        EmployeeListPresenter(view, viewModel, get()).onSaveInstanceState()

        verify(viewModel).saveState()
    }
}