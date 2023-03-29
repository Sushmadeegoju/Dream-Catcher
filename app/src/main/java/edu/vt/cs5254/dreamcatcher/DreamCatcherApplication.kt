package edu.vt.cs5254.dreamcatcher

import android.app.Application

class DreamCatcherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        DreamRepository.initialize(applicationContext)
    }
}