package net.yuzumone.bergamio.di

import dagger.Subcomponent
import net.yuzumone.bergamio.activity.AppShortcutActivity
import net.yuzumone.bergamio.activity.MainActivity

@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun inject(activity: AppShortcutActivity)

    fun plus(module: FragmentModule): FragmentComponent
}