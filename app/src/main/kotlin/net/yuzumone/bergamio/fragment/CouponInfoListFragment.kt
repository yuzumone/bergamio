package net.yuzumone.bergamio.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import net.yuzumone.bergamio.BuildConfig
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.api.MioponClient
import net.yuzumone.bergamio.databinding.FragmentCouponInfoListBinding
import net.yuzumone.bergamio.databinding.ItemCouponInfoBinding
import net.yuzumone.bergamio.model.CouponInfo
import net.yuzumone.bergamio.model.HdoInfo
import net.yuzumone.bergamio.model.PacketLogInfo
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import net.yuzumone.bergamio.view.RecyclerItemClickListener
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

class CouponInfoListFragment : BaseFragment() {

    private lateinit var binding: FragmentCouponInfoListBinding
    private lateinit var couponInfo: ArrayList<CouponInfo>
    private lateinit var packetLogs: ArrayList<PacketLogInfo>
    private lateinit var adapter: CouponInfoAdapter
    @Inject lateinit var client: MioponClient
    @Inject lateinit var compositeSubscription: CompositeSubscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        couponInfo = ArrayList<CouponInfo>()
        packetLogs = ArrayList<PacketLogInfo>()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCouponInfoListBinding.inflate(inflater, container, false)
        getComponent().inject(this)
        initView()
        return binding.root
    }

    private fun initView() {
        activity.title = activity.getString(R.string.app_name)
        binding.swipeRefresh.isEnabled = false
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        adapter = CouponInfoAdapter(activity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object: RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val info = adapter.getItem(position)
                        val log = packetLogs[position]
                        val fragment = ViewPagerFragment.newInstance(info, log)
                        fragmentManager.beginTransaction().replace(R.id.content, fragment)
                                .addToBackStack(null).commit()
                    }
                })
        )
    }

    private fun fetchCoupon(developer: String, token: String): Subscription {
        return client.getCoupon(developer, token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding.swipeRefresh.isRefreshing = true }
                .doOnCompleted { binding.swipeRefresh.isRefreshing = false }
                .subscribe (
                        { response ->
                            couponInfo = ArrayList<CouponInfo>(response.couponInfo)
                            adapter.addAllWithNotify(couponInfo)
                        },
                        { error ->
                            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    private fun fetchLog(developer: String, token: String): Subscription {
        return client.getLog(developer, token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { response ->
                            packetLogs = ArrayList<PacketLogInfo>(response.packetLogInfo)
                        },
                        { error ->
                            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (couponInfo.size == 0 && packetLogs.size == 0) {
            val dev = BuildConfig.DEVELOPER_ID
            val token = PreferenceUtil.loadToken(activity)
            compositeSubscription.add(fetchCoupon(dev, token))
            compositeSubscription.add(fetchLog(dev, token))
        } else {
            adapter.addAllWithNotify(couponInfo)
        }
    }

    override fun onDestroyView() {
        compositeSubscription.unsubscribe()
        super.onDestroyView()
    }

    class CouponInfoAdapter(context: Context) : ArrayRecyclerAdapter<CouponInfo,
            BindingHolder<ItemCouponInfoBinding>>(context) {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingHolder<ItemCouponInfoBinding> {
            return BindingHolder(context, parent!!, R.layout.item_coupon_info)
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemCouponInfoBinding>?, position: Int) {
            val item = getItem(position)
            holder?.binding?.let { binding ->
                binding.info = item
                val number = item.hdoInfo.joinToString(transform = HdoInfo::number, separator = "\n")
                binding.textNumber.text = number
                val total = item.coupon.sumBy { it.volume }
                binding.textTotal.text = context.getString(R.string.mb, total)
            }
        }
    }
}