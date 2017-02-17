package net.yuzumone.bergamio.di

import dagger.Subcomponent
import net.yuzumone.bergamio.fragment.CouponInfoListFragment

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun inject(fragment: CouponInfoListFragment)
}