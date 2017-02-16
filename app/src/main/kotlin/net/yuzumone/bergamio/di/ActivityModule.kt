package net.yuzumone.bergamio.di

import android.content.Context
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val activity: AppCompatActivity) {

    @Provides
    fun provideActivity(): AppCompatActivity {
        return activity
    }

    @Provides
    fun provideContext(): Context {
        return activity
    }
}