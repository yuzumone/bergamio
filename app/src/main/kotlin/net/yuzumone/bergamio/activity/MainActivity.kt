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

class MainActivity : AppCompatActivity() {

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
        }
    }

    fun getComponent(): ActivityComponent {
        val mainApplication = application as MainApp
        return mainApplication.appComponent.plus(ActivityModule(this))
    }

}
