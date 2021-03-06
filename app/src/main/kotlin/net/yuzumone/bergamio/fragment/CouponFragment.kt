package net.yuzumone.bergamio.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.FragmentCouponBinding
import net.yuzumone.bergamio.databinding.ItemCouponBinding
import net.yuzumone.bergamio.model.Coupon
import net.yuzumone.bergamio.view.ArrayRecyclerAdapter
import net.yuzumone.bergamio.view.BindingHolder
import java.util.*

class CouponFragment : BaseFragment() {

    private lateinit var binding: FragmentCouponBinding
    private val coupons: List<Coupon> by lazy {
        arguments!!.getParcelableArrayList<Coupon>(ARG_COUPON)
    }

    companion object {
        private const val ARG_COUPON = "coupon"
        fun newInstance(coupon: ArrayList<Coupon>): CouponFragment {
            return CouponFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_COUPON, coupon)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupon, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val adapter = CouponAdapter(activity!!)
        adapter.addAllWithNotify(coupons)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    class CouponAdapter(context: Context) : ArrayRecyclerAdapter<Coupon,
            BindingHolder<ItemCouponBinding>>(context) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemCouponBinding> {
            return BindingHolder(context, parent, R.layout.item_coupon)
        }

        override fun onBindViewHolder(holder: BindingHolder<ItemCouponBinding>, position: Int) {
            val item = getItem(position)
            holder.binding.coupon = item
        }
    }
}