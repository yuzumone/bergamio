package net.yuzumone.bergamio.di

import dagger.Component
import net.yuzumone.bergamio.MainApp
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: MainApp)

    fun plus(module: ActivityModule): ActivityComponent
}