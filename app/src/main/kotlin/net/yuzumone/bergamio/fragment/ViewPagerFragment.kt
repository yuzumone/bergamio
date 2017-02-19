package net.yuzumone.bergamio.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.bergamio.databinding.FragmentViewpagerBinding
import net.yuzumone.bergamio.model.CouponInfo
import java.util.*

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewpagerBinding
    private lateinit var couponInfo: CouponInfo

    companion object {
        val ARG_COUPON_INFO = "coupon_info"
        fun newInstance(couponInfo: CouponInfo): ViewPagerFragment {
            return ViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COUPON_INFO, couponInfo)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            couponInfo = arguments.getParcelable(ARG_COUPON_INFO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentViewpagerBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        activity.title = couponInfo.hddServiceCode
        val adapter = ViewPagerAdapter(fragmentManager)
        binding.pager.adapter = adapter
        binding.tab.setupWithViewPager(binding.pager)
        couponInfo.hdoInfo.forEach { hdoInfo ->
            val fragment = HdoInfoFragment.newInstance(hdoInfo)
            adapter.add(hdoInfo.number, fragment)
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