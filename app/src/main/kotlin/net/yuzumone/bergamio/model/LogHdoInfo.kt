package net.yuzumone.bergamio.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class LogHdoInfo(val hdoServiceCode: String, val packetLog: List<PacketLog>): PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelLogHdoInfo.CREATOR
    }
}