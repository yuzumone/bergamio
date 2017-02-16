package net.yuzumone.bergamio

import android.app.Application
import net.yuzumone.bergamio.di.AppComponent
import net.yuzumone.bergamio.di.AppModule
import net.yuzumone.bergamio.di.DaggerAppComponent

class MainApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)
    }
}