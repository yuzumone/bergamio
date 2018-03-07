package net.yuzumone.bergamio.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class HdoInfo(
        val hdoServiceCode: String,
        val number: String,
        val iccid: String,
        val regulation: Boolean,
        val sms: Boolean,
        val voice: Boolean,
        val couponUse: Boolean,
        val coupon: List<Coupon>
): Parcelable