package net.yuzumone.bergamio.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import net.yuzumone.bergamio.MainApp
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.ActivityMainBinding
import net.yuzumone.bergamio.di.ActivityComponent
import net.yuzumone.bergamio.di.ActivityModule
import net.yuzumone.bergamio.fragment.CouponInfoListFragment
import net.yuzumone.bergamio.fragment.HdoInfoFragment
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.OnToggleElevationListener

class MainActivity : AppCompatActivity(), OnToggleElevationListener, HdoInfoFragment.OnRefreshCouponInfoListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
        getComponent().inject(this)

        if (PreferenceUtil.hasAvailableToken(this)) {
            if (savedInstanceState == null) {
                val fragment = CouponInfoListFragment()
                supportFragmentManager.beginTransaction()
                        .add(R.id.content, fragment, CouponInfoListFragment.TAG).commit()
            }
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onRefresh() {
        val f = supportFragmentManager.findFragmentByTag(CouponInfoListFragment.TAG)
        if (f != null && f is CouponInfoListFragment) {
            f.notifyShouldRefresh()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onToggleElevation(bool: Boolean) {
        val elevation = if (bool) resources.getDimension(R.dimen.elevation) else 0F
        binding.toolbar.elevation = elevation
    }

    fun getComponent(): ActivityComponent {
        val mainApplication = application as MainApp
        return mainApplication.appComponent.plus(ActivityModule(this))
    }

}
