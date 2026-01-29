package com.fishing.conditions

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FishingApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
