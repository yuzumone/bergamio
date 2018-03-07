package net.yuzumone.bergamio.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CouponInfo(
        val hddServiceCode: String,
        val plan: String,
        val hdoInfo: List<HdoInfo>,
        val coupon: List<Coupon>
): Parcelable