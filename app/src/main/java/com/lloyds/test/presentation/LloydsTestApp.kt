package com.lloyds.test.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LloydsTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
//        YouTubePlayerView.setYouTubePlayerViewTheme(com.google.android.material.R.style.Theme_MaterialComponents_Light)
    }
}
