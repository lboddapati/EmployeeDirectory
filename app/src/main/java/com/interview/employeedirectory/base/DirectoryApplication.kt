package com.interview.employeedirectory.base

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DirectoryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DirectoryApplication)
            modules(
                applicationModule,
                presenterFactoryModule
            )
        }
    }
}