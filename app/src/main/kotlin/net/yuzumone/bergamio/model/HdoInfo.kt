package net.yuzumone.bergamio.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class HdoInfo(
        val hdoServiceCode: String,
        val number: String,
        val iccid: String,
        val regulation: Boolean,
        val sms: Boolean,
        val voice: Boolean,
        val couponUse: Boolean,
        val coupon: List<Coupon>
): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelHdoInfo.CREATOR
    }
}