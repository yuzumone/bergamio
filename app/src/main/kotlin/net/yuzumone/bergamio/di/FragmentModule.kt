package net.yuzumone.bergamio.di

import android.content.Context
import android.support.v4.app.Fragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(val fragment: Fragment) {

    @Provides
    fun provideContext(): Context {
        return fragment.context!!
    }
}