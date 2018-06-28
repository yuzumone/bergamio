package net.yuzumone.bergamio.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.FragmentLicenseBinding

class LicenseFragment : Fragment() {

    private lateinit var binding: FragmentLicenseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_license, container, false)
        binding.web.loadUrl("file:///android_asset/license.html")
        return binding.root
    }
}