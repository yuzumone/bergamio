package net.yuzumone.bergamio.di

import dagger.Subcomponent
import net.yuzumone.bergamio.fragment.CouponInfoListFragment
import net.yuzumone.bergamio.fragment.HdoInfoFragment

@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun inject(fragment: CouponInfoListFragment)

    fun inject(fragment: HdoInfoFragment)
}