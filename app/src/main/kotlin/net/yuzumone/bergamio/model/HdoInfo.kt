package net.yuzumone.bergamio.model

data class HdoInfo(
        val hdoServiceCode: String,
        val number: String,
        val iccid: String,
        val regulation: Boolean,
        val sms: Boolean,
        val voice: Boolean,
        val couponUse: Boolean,
        val coupon: List<Coupon>
)