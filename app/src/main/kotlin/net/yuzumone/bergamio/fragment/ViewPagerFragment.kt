package net.yuzumone.bergamio.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.FragmentViewpagerBinding
import net.yuzumone.bergamio.model.CouponInfo
import net.yuzumone.bergamio.model.PacketLogInfo
import net.yuzumone.bergamio.view.OnToggleElevationListener
import java.util.*

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewpagerBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var listener: OnToggleElevationListener
    private val couponInfo: CouponInfo by lazy {
        arguments!!.getParcelable<CouponInfo>(ARG_COUPON_INFO)
    }
    private val packetLogInfo: PacketLogInfo by lazy {
        arguments!!.getParcelable<PacketLogInfo>(ARG_PACKET_LOG)
    }

    companion object {
        private const val ARG_COUPON_INFO = "coupon_info"
        private const val ARG_PACKET_LOG = "packet_log"
        fun newInstance(couponInfo: CouponInfo, packetLog: PacketLogInfo): ViewPagerFragment {
            return ViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COUPON_INFO, couponInfo)
                    putParcelable(ARG_PACKET_LOG, packetLog)
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnToggleElevationListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_viewpager, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        activity!!.title = couponInfo.hddServiceCode
        listener.onToggleElevation(false)
        adapter = ViewPagerAdapter(childFragmentManager)
        binding.pager.adapter = adapter
        binding.tab.setupWithViewPager(binding.pager)
        val couponFragment = CouponFragment.newInstance(ArrayList(couponInfo.coupon))
        adapter.add("Coupon", couponFragment)
        couponInfo.hdoInfo.forEachIndexed { i, hdoInfo ->
            val packetLogs = packetLogInfo.hdoInfo[i].packetLog
            val hdoFragment = HdoInfoFragment.newInstance(hdoInfo, ArrayList(packetLogs))
            adapter.add(hdoInfo.number, hdoFragment)
        }
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

        fun add(title: String, fragment: Fragment) {
            fragments.add(fragment)
            titles.add(title)
            notifyDataSetChanged()
        }
    }
}