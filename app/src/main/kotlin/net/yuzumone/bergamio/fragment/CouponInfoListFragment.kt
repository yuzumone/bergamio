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
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

class CouponInfoListFragment : BaseFragment() {

    private val ARG_COUPON_INFO = "coupon_info"
    private lateinit var binding: FragmentCouponInfoListBinding
    private lateinit var couponInfo: ArrayList<CouponInfo>
    private lateinit var adapter: CouponInfoAdapter
    @Inject lateinit var client: MioponClient
    @Inject lateinit var compositeSubscription: CompositeSubscription

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCouponInfoListBinding.inflate(inflater, container, false)
        getComponent().inject(this)
        initView()
        return binding.root
    }

    private fun initView() {
        adapter = CouponInfoAdapter(activity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun fetchCoupon(developer: String, token: String): Subscription {
        return client.getCoupon(developer, token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            val dev = BuildConfig.DEVELOPER_ID
            val token = PreferenceUtil.loadToken(activity)
            compositeSubscription.add(fetchCoupon(dev, token))
        } else {
            couponInfo = savedInstanceState.getParcelableArrayList(ARG_COUPON_INFO)
            adapter.addAllWithNotify(couponInfo)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelableArrayList(ARG_COUPON_INFO, couponInfo)
    }

    override fun onDestroy() {
        compositeSubscription.unsubscribe()
        super.onDestroy()
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