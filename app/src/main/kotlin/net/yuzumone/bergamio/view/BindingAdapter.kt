package net.yuzumone.bergamio.view

import android.databinding.BindingAdapter
import android.widget.TextView
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.model.Coupon
import net.yuzumone.bergamio.model.CouponInfo
import net.yuzumone.bergamio.model.HdoInfo

object BindingAdapter {

    @BindingAdapter("usage")
    @JvmStatic
    fun setUsage(view: TextView, usage: Int) {
        view.text = view.context.getString(R.string.mb, usage)
    }

    @BindingAdapter("type_expire")
    @JvmStatic
    fun setTypeExpire(view: TextView, coupon: Coupon) {
        view.text = view.context.getString(R.string.type_expire, coupon.type, coupon.expire)
    }

    @BindingAdapter("number")
    @JvmStatic
    fun setNumber(view: TextView, couponInfo: CouponInfo) {
        view.text = couponInfo.hdoInfo.joinToString(transform = HdoInfo::number, separator = "\n")
    }

    @BindingAdapter("total")
    @JvmStatic
    fun setTotal(view: TextView, couponInfo: CouponInfo) {
        view.text = view.context.getString(R.string.mb, couponInfo.coupon.sumBy { it.volume })
    }
}