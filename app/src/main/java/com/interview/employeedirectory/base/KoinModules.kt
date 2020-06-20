package com.interview.employeedirectory.base

import com.interview.employeedirectory.datalayer.DataRepository
import com.interview.employeedirectory.datalayer.DataRepositoryImpl
import org.koin.dsl.module

val applicationModule = module {
    single<DataRepository> { DataRepositoryImpl() }
    single { SubscriptionConfig.createDefaultConfig() }
}