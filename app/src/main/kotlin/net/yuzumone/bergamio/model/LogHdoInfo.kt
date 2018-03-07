package net.yuzumone.bergamio.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class LogHdoInfo(val hdoServiceCode: String, val packetLog: List<PacketLog>): Parcelable