package net.yuzumone.bergamio.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PacketLog(val date: String, val withCoupon: Int, val withoutCoupon: Int): Parcelable