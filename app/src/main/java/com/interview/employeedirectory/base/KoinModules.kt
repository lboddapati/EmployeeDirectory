package com.interview.employeedirectory.base

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.room.Room
import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.datalayer.DataRepositoryImpl
import com.interview.employeedirectory.datalayer.memorycache.MemoryCacheRepository
import com.interview.employeedirectory.datalayer.persistentcache.PersistentCacheRepository
import com.interview.employeedirectory.models.Employee
import com.interview.employeedirectory.ui.employeedetail.EmployeeDetailContract
import com.interview.employeedirectory.ui.employeedetail.EmployeeDetailPresenter
import com.interview.employeedirectory.ui.employeelist.EmployeeListContract
import com.interview.employeedirectory.ui.employeelist.EmployeeListPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val SHARED_PREFS_NAME = "EmployeeDirectorySharedPrefs"

val applicationModule = module {
    single<DataRepository> { DataRepositoryImpl() }
    single {
        Room.databaseBuilder(
            androidContext(),
            PersistentCacheRepository::class.java,
            "persistentCache"
        ).build()
    }
    single { MemoryCacheRepository() }
    single { androidContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE) }
    single { SubscriptionConfig.createDefaultConfig() }
    factory { (lifecycle: Lifecycle) -> LifecycleAwareSubscriptionManager(lifecycle) }
}

val presenterFactoryModule = module {
    factory<EmployeeListContract.Presenter> {
            (view: EmployeeListContract.View,
                lifecycle: Lifecycle) ->
        EmployeeListPresenter(
            view = view,
            subscriptionManager = get { parametersOf(lifecycle) }
        ).apply { lifecycle.addObserver(this) }
    }

    factory<EmployeeDetailContract.Presenter> {
        (employee: Employee?,
            view: EmployeeDetailContract.View,
            lifecycle: Lifecycle) ->
        EmployeeDetailPresenter(
            employee,
            view = view,
            subscriptionManager = get { parametersOf(lifecycle) }
        ).apply { lifecycle.addObserver(this) }
    }
}