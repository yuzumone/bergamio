package net.yuzumone.bergamio.view

import android.databinding.BindingAdapter
import android.widget.TextView
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.model.Coupon

object BindingAdapter {

    @BindingAdapter("app:usage")
    @JvmStatic
    fun setUsage(view: TextView, usage: Int) {
        view.text = view.context.getString(R.string.mb, usage)
    }

    @BindingAdapter("app:type_expire")
    @JvmStatic
    fun setTypeExpire(view: TextView, coupon: Coupon) {
        view.text = view.context.getString(R.string.type_expire, coupon.type, coupon.expire)
    }
}