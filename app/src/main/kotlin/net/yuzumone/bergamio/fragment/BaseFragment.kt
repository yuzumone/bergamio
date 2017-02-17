package net.yuzumone.bergamio.fragment

import android.support.v4.app.Fragment
import net.yuzumone.bergamio.activity.MainActivity
import net.yuzumone.bergamio.di.FragmentComponent
import net.yuzumone.bergamio.di.FragmentModule

abstract class BaseFragment : Fragment() {

    fun getComponent(): FragmentComponent {
        val mainActivity = activity as MainActivity
        return mainActivity.getComponent().plus(FragmentModule(this))
    }
}