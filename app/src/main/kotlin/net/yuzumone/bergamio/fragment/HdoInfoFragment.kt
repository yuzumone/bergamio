package net.yuzumone.bergamio.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.yuzumone.bergamio.BuildConfig
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.api.MioponClient
import net.yuzumone.bergamio.databinding.FragmentHdoInfoBinding
import net.yuzumone.bergamio.databinding.ItemPacketLogBinding
import net.yuzumone.bergamio.model.*
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import java.util.*
import javax.inject.Inject

class HdoInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentHdoInfoBinding
    private lateinit var listener: OnRefreshCouponInfoListener
    @Inject lateinit var client: MioponClient
    @Inject lateinit var compositeDisposable: CompositeDisposable
    private val hdoInfo: HdoInfo by lazy {
        arguments!!.getParcelable<HdoInfo>(ARG_HDO_INFO)
    }
    private val packetLogs: ArrayList<PacketLog> by lazy {
        arguments!!.getParcelableArrayList<PacketLog>(ARG_PACKET_LOG)
    }

    companion object {
        private const val ARG_HDO_INFO = "hdo_info"
        private const val ARG_PACKET_LOG = "packet_log"
        private const val CONFIRM_DIALOG_REQUEST = 3939
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hdo_info, container, false)
        getComponent().inject(this)
        initView()
        return binding.root
    }

    private fun initView() {
        val adapter = PacketLogAdapter(activity!!)
        adapter.addAllWithNotify(packetLogs)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.switchCoupon.isChecked = hdoInfo.couponUse
        binding.switchCoupon.setOnClickListener { view ->
            val switch = view as Switch
            val bool = switch.isChecked
            switch.isChecked = !bool
            val fragment = ConfirmDialogFragment
                    .newInstance(this, CONFIRM_DIALOG_REQUEST, bool, hdoInfo)
            fragment.show(fragmentManager, "confirm")
        }
        val text = if (hdoInfo.couponUse) getString(R.string.coupon_on) else getString(R.string.coupon_off)
        binding.textCoupon.text = text
        invalidateChart()
    }

    private fun invalidateChart() {
        binding.chart.apply {
            xAxis.apply {
                isEnabled = false
            }
            axisLeft.apply {
                axisMinimum = 0f
            }
            axisRight.apply {
                isEnabled = false
            }
        }
        val entries = ArrayList<Entry>()
        for ((index, value) in packetLogs.withIndex()) {
            entries.add(Entry(index.toFloat(), value.withCoupon.toFloat()))
        }
        val dataSet = LineDataSet(entries, "withCoupon")
        val lineData = LineData(dataSet)
        binding.chart.data = lineData
        binding.chart.invalidate()
    }

    private fun createBody(hdo: String, bool: Boolean): ToggleCouponInfo {
        val toggle = Toggle(hdo, bool)
        val hdoInfo = ToggleHdoInfo(arrayListOf(toggle))
        return ToggleCouponInfo(arrayListOf(hdoInfo))
    }

    private fun putToggleCoupon(developer: String, token: String, body: ToggleCouponInfo): Disposable {
        return client.putToggleCoupon(developer, token, body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CONFIRM_DIALOG_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) return
                    val useCoupon = data
                            .getBooleanExtra(ConfirmDialogFragment.ARG_USE_COUPON, false)
                    toggle(useCoupon)
                }
            }
        }
    }

    private fun toggle(useCoupon: Boolean) {
        val dev = BuildConfig.DEVELOPER_ID
        val token = PreferenceUtil(activity!!).token
        val body = createBody(hdoInfo.hdoServiceCode, useCoupon)
        compositeDisposable.add(putToggleCoupon(dev, token, body))
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    interface OnRefreshCouponInfoListener {
        fun onRefresh()
    }

    class PacketLogAdapter(context: Context) : ArrayRecyclerAdapter<PacketLog,
            BindingHolder<ItemPacketLogBinding>>(context) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemPacketLogBinding> {
            return BindingHolder(context, parent, R.layout.item_packet_log)
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemPacketLogBinding>, position: Int) {
            val item = getItem(position)
            holder.binding.log = item
        }
    }
}