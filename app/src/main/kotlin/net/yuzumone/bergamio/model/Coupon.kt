package net.yuzumone.bergamio.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Coupon(val volume: Int, val expire: String?, val type: String): Parcelable