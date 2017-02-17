package net.yuzumone.bergamio.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class CouponInfo(
        val hddServiceCode: String,
        val plan: String,
        val hdoInfo: List<HdoInfo>,
        val coupon: List<Coupon>
): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelCouponInfo.CREATOR
    }
}