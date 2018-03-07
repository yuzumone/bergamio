package net.yuzumone.bergamio.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PacketLogInfo(
        val hddServiceCode: String,
        val plan: String,
        val hdoInfo: List<LogHdoInfo>
): Parcelable