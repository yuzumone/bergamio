package net.yuzumone.bergamio.model

data class CouponInfo(
        val hddServiceCode: String,
        val plan: String,
        val hdoInfo: List<HdoInfo>,
        val coupon: List<Coupon>
)