package net.yuzumone.bergamio.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.yuzumone.bergamio.MainApp
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.ActivityAppShortcutBinding
import net.yuzumone.bergamio.di.ActivityComponent
import net.yuzumone.bergamio.di.ActivityModule

class AppShortcutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppShortcutBinding
    private val number: String by lazy {
        intent.getStringExtra(ARG_NUMBER)
    }

    companion object {
        private const val ARG_NUMBER = "number"
        fun creteIntent(context: Context, number: String): Intent {
            val intent = Intent(context, AppShortcutActivity::class.java)
            intent.action = Intent.ACTION_DEFAULT
            intent.putExtra(ARG_NUMBER, number)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_shortcut)
        getComponent().inject(this)
        // TODO
    }

    private fun getComponent(): ActivityComponent {
        val mainApplication = application as MainApp
        return mainApplication.appComponent.plus(ActivityModule(this))
    }
}