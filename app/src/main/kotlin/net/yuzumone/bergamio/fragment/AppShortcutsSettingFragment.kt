package net.yuzumone.bergamio.fragment

import android.annotation.TargetApi
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.databinding.DataBindingUtil
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.activity.AppShortcutActivity
import net.yuzumone.bergamio.databinding.FragmentAppShortcutsSettingBinding
import net.yuzumone.bergamio.databinding.ItemHdoInfoBinding
import net.yuzumone.bergamio.model.CouponInfo
import net.yuzumone.bergamio.model.HdoInfo
import java.util.*

class AppShortcutsSettingFragment : Fragment() {

    private lateinit var binding: FragmentAppShortcutsSettingBinding
    private val childViewList = ArrayList<ItemHdoInfoBinding>()
    private val couponInfoList: List<CouponInfo> by lazy {
        arguments!!.getParcelableArrayList<CouponInfo>(ARG_COUPON_INFO)
    }

    companion object {
        private const val ARG_COUPON_INFO = "coupon_info"
        fun newInstance(couponInfo: ArrayList<CouponInfo>): AppShortcutsSettingFragment {
            return AppShortcutsSettingFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_COUPON_INFO, couponInfo)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_shortcuts_setting, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeList()
        binding.buttonSave.setOnClickListener {
            childViewList.forEach { child ->
                if (child.checkboxShortcuts.isChecked) {
                    addShortcut(child.hdo)
                } else {
                    removeShortcut(child.hdo)
                }
            }
            activity!!.onBackPressed()
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun initializeList() {
        val shortcutManager = activity!!.getSystemService(ShortcutManager::class.java)
        val shortcuts = shortcutManager.dynamicShortcuts
        val inflater = LayoutInflater.from(activity)
        couponInfoList.forEach { couponInfo ->
            couponInfo.hdoInfo.forEach { hdoInfo ->
                val child = DataBindingUtil.inflate<ItemHdoInfoBinding>(inflater,
                        R.layout.item_hdo_info, binding.linearList, false)
                child.hdo = hdoInfo
                shortcuts.forEach { shortcut ->
                    if (hdoInfo.number == shortcut.id) {
                        child.checkboxShortcuts.isChecked = true
                    }
                }
                binding.linearList.addView(child.root)
                childViewList.add(child)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun addShortcut(hdoInfo: HdoInfo?) {
        if (hdoInfo == null) return
        val intent = AppShortcutActivity.creteIntent(activity!!, hdoInfo.number)
        val shortcut = ShortcutInfo.Builder(activity, hdoInfo.number)
                .setShortLabel(hdoInfo.number)
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_app_shortcut))
                .setIntent(intent)
                .build()
        val shortcutManager = activity!!.getSystemService(ShortcutManager::class.java)
        shortcutManager.dynamicShortcuts = Arrays.asList(shortcut)
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun removeShortcut(hdoInfo: HdoInfo?) {
        if (hdoInfo == null) return
        val shortcutManager = activity!!.getSystemService(ShortcutManager::class.java)
        shortcutManager.removeDynamicShortcuts(Arrays.asList(hdoInfo.number))
    }
}