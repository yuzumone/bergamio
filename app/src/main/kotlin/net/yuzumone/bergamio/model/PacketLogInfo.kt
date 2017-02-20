package net.yuzumone.bergamio.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class PacketLogInfo(
        val hddServiceCode: String,
        val plan: String,
        val hdoInfo: List<LogHdoInfo>
): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelPacketLogInfo.CREATOR
    }
}