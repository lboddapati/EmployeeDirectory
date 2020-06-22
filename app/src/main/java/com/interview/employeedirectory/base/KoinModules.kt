package com.interview.employeedirectory.base

import androidx.lifecycle.Lifecycle
import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.datalayer.DataRepositoryImpl
import com.interview.employeedirectory.ui.employeelist.EmployeeListContract
import com.interview.employeedirectory.ui.employeelist.EmployeeListPresenter
import com.interview.employeedirectory.ui.employeelist.EmployeeListViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val applicationModule = module {
    single<DataRepository> { DataRepositoryImpl() }
    single { SubscriptionConfig.createDefaultConfig() }
    factory { (lifecycle: Lifecycle) -> LifecycleAwareSubscriptionManager(lifecycle) }
}

val presenterFactoryModule = module {
    factory<EmployeeListContract.Presenter> {
            (view: EmployeeListContract.View,
                viewModel: EmployeeListViewModel,
                lifecycle: Lifecycle) ->
        EmployeeListPresenter(
            view = view,
            viewModel = viewModel,
            subscriptionManager = get { parametersOf(lifecycle) }
        ).apply { lifecycle.addObserver(this) }
    }
}