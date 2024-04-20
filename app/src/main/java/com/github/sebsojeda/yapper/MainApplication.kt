package com.github.sebsojeda.yapper

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        TODO("Not yet implemented")
    }
}
