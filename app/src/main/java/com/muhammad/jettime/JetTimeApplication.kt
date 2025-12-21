package com.muhammad.jettime

import android.app.Application
import com.muhammad.jettime.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JetTimeApplication : Application(){
    companion object{
        lateinit var INSTANCE : JetTimeApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@JetTimeApplication)
            androidLogger()
            modules(appModule)
        }
    }
}