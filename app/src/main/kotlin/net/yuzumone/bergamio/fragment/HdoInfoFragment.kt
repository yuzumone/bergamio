package net.yuzumone.bergamio.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import net.yuzumone.bergamio.BuildConfig
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.api.MioponClient
import net.yuzumone.bergamio.databinding.FragmentHdoInfoBinding
import net.yuzumone.bergamio.databinding.ItemPacketLogBinding
import net.yuzumone.bergamio.model.*
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

class HdoInfoFragment : BaseFragment(), ConfirmDialogFragment.OnConfirmListener {

    private lateinit var binding: FragmentHdoInfoBinding
    private lateinit var hdoInfo: HdoInfo
    private lateinit var packetLogs: ArrayList<PacketLog>
    private lateinit var listener: OnRefreshCouponInfoListener
    @Inject lateinit var client: MioponClient
    @Inject lateinit var compositeSubscription: CompositeSubscription

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnRefreshCouponInfoListener) {
            listener = context
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
        getComponent().inject(this)
        initView()
        return binding.root
    }

    private fun initView() {
        val adapter = PacketLogAdapter(activity)
        adapter.addAllWithNotify(packetLogs)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.switchCoupon.isChecked = hdoInfo.couponUse
        binding.switchCoupon.setOnClickListener { view ->
            val switch = view as Switch
            val bool = switch.isChecked
            switch.isChecked = !bool
            val fragment = ConfirmDialogFragment.newInstance(this, bool)
            fragment.show(fragmentManager, "confirm")
        }
        val text = if (hdoInfo.couponUse) getString(R.string.coupon_on) else getString(R.string.coupon_off)
        binding.textCoupon.text = text
    }

    private fun createBody(hdo: String, bool: Boolean): ToggleCouponInfo {
        val toggle = Toggle(hdo, bool)
        val hdoInfo = ToggleHdoInfo(arrayListOf(toggle))
        return ToggleCouponInfo(arrayListOf(hdoInfo))
    }

    private fun putToggleCoupon(developer: String, token: String, body: ToggleCouponInfo): Subscription {
        return client.putToggleCoupon(developer, token, body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { response ->
                            Toast.makeText(activity, getString(R.string.success), Toast.LENGTH_SHORT).show()
                            val bool = binding.switchCoupon.isChecked
                            binding.switchCoupon.isChecked = !bool
                            val text = if (bool) getString(R.string.coupon_off) else getString(R.string.coupon_on)
                            binding.textCoupon.text = text
                            listener.onRefresh()
                        },
                        { error ->
                            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    override fun onToggleCoupon(bool: Boolean) {
        val dev = BuildConfig.DEVELOPER_ID
        val token = PreferenceUtil.loadToken(activity)
        val body = createBody(hdoInfo.hdoServiceCode, bool)
        compositeSubscription.add(putToggleCoupon(dev, token, body))
    }

    override fun onDestroyView() {
        compositeSubscription.unsubscribe()
        super.onDestroyView()
    }

    interface OnRefreshCouponInfoListener {
        fun onRefresh()
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