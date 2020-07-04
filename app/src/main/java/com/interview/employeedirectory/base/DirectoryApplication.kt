package com.interview.employeedirectory.base

import android.app.Application
import com.interview.employeedirectory.datalayer.cache.PersistentCache
import org.koin.android.ext.android.get
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

        get<PersistentCache>().clearExpiredItems()
    }
}