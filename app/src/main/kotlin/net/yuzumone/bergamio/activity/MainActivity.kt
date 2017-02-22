package net.yuzumone.bergamio.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.yuzumone.bergamio.MainApp
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.ActivityMainBinding
import net.yuzumone.bergamio.di.ActivityComponent
import net.yuzumone.bergamio.di.ActivityModule
import net.yuzumone.bergamio.fragment.CouponInfoListFragment
import net.yuzumone.bergamio.util.PreferenceUtil
import net.yuzumone.bergamio.view.OnToggleElevationListener

class MainActivity : AppCompatActivity(), OnToggleElevationListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        getComponent().inject(this)

        if (PreferenceUtil.hasAvailableToken(this)) {
            if (savedInstanceState == null) {
                val fragment = CouponInfoListFragment()
                supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
            }
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
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
