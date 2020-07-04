package com.interview.employeedirectory.base

import android.app.Application
import com.interview.employeedirectory.datalayer.memorycache.MemoryCacheRepository
import com.interview.employeedirectory.datalayer.persistentcache.PersistentCacheRepository
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DirectoryApplication: Application() {

    private lateinit var memoryCacheRepository: MemoryCacheRepository
    private lateinit var persistentCacheRepository: PersistentCacheRepository

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DirectoryApplication)
            modules(
                applicationModule,
                presenterFactoryModule
            )
        }

        memoryCacheRepository = get()
        persistentCacheRepository = get()
        persistentCacheRepository.clearExpiredItems()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        memoryCacheRepository.clearExpiredItems()
        persistentCacheRepository.clearExpiredItems()
    }
}