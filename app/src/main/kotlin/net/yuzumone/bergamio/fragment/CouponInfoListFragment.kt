package net.yuzumone.bergamio.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import net.yuzumone.bergamio.BuildConfig
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.api.MioponClient
import net.yuzumone.bergamio.databinding.FragmentCouponInfoListBinding
import net.yuzumone.bergamio.databinding.ItemCouponInfoBinding
import net.yuzumone.bergamio.model.CouponInfo
import net.yuzumone.bergamio.model.CouponResult
import net.yuzumone.bergamio.model.LogResult
import net.yuzumone.bergamio.model.PacketLogInfo
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import net.yuzumone.bergamio.view.OnToggleElevationListener
import net.yuzumone.bergamio.view.RecyclerItemClickListener
import rx.Observable
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
    private lateinit var listener: OnToggleElevationListener
    private var shouldRefresh = false
    @Inject lateinit var client: MioponClient
    @Inject lateinit var compositeSubscription: CompositeSubscription

    companion object {
        val TAG: String = CouponInfoListFragment::class.java.simpleName
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnToggleElevationListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        couponInfo = ArrayList()
        packetLogs = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_info_list, container, false)
        getComponent().inject(this)
        initView()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initView() {
        activity!!.title = getString(R.string.app_name)
        listener.onToggleElevation(true)
        binding.swipeRefresh.isEnabled = false
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        adapter = CouponInfoAdapter(activity!!)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity!!, object: RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val info = adapter.getItem(position)
                        val log = packetLogs[position]
                        val fragment = ViewPagerFragment.newInstance(info, log)
                        fragmentManager!!.beginTransaction().replace(R.id.content, fragment)
                                .addToBackStack(null).commit()
                    }
                })
        )
    }

    private fun fetch(developer: String, token: String): Subscription {
        return Observable.zip(
                client.getCoupon(developer, token),
                client.getLog(developer, token),
                {coupon, log -> createResponseData(coupon, log)})
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding.swipeRefresh.isRefreshing = true }
                .doOnCompleted { binding.swipeRefresh.isRefreshing = false }
                .subscribe (
                        { (coupon, log) ->
                            couponInfo = ArrayList(coupon.couponInfo)
                            adapter.addAllWithNotify(couponInfo)
                            shouldRefresh = false
                            packetLogs = ArrayList(log.packetLogInfo)
                        },
                        { error ->
                            Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    private fun createResponseData(coupon: CouponResult, log: LogResult): ResponseData {
        return ResponseData(coupon, log)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if ((couponInfo.isEmpty() && packetLogs.isEmpty()) || shouldRefresh) {
            val dev = BuildConfig.DEVELOPER_ID
            val token = PreferenceUtil(activity!!).token
            compositeSubscription.add(fetch(dev, token))
        } else {
            adapter.addAllWithNotify(couponInfo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_settings -> {
                val fragment = AppShortcutsSettingFragment.newInstance(couponInfo)
                fragmentManager!!.beginTransaction().replace(R.id.content, fragment)
                        .addToBackStack(null).commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        compositeSubscription.unsubscribe()
        super.onDestroyView()
    }

    fun notifyShouldRefresh() {
        shouldRefresh = true
    }

    data class ResponseData(val coupon: CouponResult, val log: LogResult)

    class CouponInfoAdapter(context: Context) : ArrayRecyclerAdapter<CouponInfo,
            BindingHolder<ItemCouponInfoBinding>>(context) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemCouponInfoBinding> {
            return BindingHolder(context, parent, R.layout.item_coupon_info)
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemCouponInfoBinding>, position: Int) {
            val item = getItem(position)
            holder.binding.info = item
        }
    }
}