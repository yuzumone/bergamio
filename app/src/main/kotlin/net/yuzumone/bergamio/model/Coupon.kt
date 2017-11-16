package net.yuzumone.bergamio.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Coupon(val volume: Int, val expire: String?, val type: String): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelCoupon.CREATOR
    }
}