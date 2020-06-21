package com.interview.employeedirectory.base

import com.interview.employeedirectory.datalayer.DataRepository
import com.nhaarman.mockitokotlin2.mock
import org.koin.dsl.module

val testApplicationModule = module {
    single<DataRepository> { mock() }
    single { SubscriptionConfig.createTestConfig() }
    factory { LifecycleAwareSubscriptionManager(mock()) }
}