package net.yuzumone.bergamio.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class PacketLog(val date: String, val withCoupon: Int, val withoutCoupon: Int): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelPacketLog.CREATOR
    }
}