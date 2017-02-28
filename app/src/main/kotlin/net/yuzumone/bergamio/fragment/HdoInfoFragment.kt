package net.yuzumone.bergamio.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.FragmentHdoInfoBinding
import net.yuzumone.bergamio.databinding.ItemPacketLogBinding
import net.yuzumone.bergamio.model.HdoInfo
import net.yuzumone.bergamio.model.PacketLog
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import java.util.*

class HdoInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentHdoInfoBinding
    private lateinit var hdoInfo: HdoInfo
    private lateinit var packetLogs: ArrayList<PacketLog>

    companion object {
        val ARG_HDO_INFO = "hdo_info"
        val ARG_PACKET_LOG = "packet_log"
        fun newInstance(hdoInfo: HdoInfo, packetLogs: ArrayList<PacketLog>): HdoInfoFragment {
            return HdoInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HDO_INFO, hdoInfo)
                    putParcelableArrayList(ARG_PACKET_LOG, packetLogs)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            hdoInfo = arguments.getParcelable(ARG_HDO_INFO)
            packetLogs = arguments.getParcelableArrayList(ARG_PACKET_LOG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHdoInfoBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val adapter = PacketLogAdapter(activity)
        adapter.addAllWithNotify(packetLogs)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    class PacketLogAdapter(context: Context) : ArrayRecyclerAdapter<PacketLog,
            BindingHolder<ItemPacketLogBinding>>(context) {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingHolder<ItemPacketLogBinding> {
            return BindingHolder(context, parent!!, R.layout.item_packet_log)
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemPacketLogBinding>, position: Int) {
            val item = getItem(position)
            holder.binding.log = item
        }
    }

}