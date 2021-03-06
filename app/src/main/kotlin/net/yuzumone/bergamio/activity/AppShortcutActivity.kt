package net.yuzumone.bergamio.activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.yuzumone.bergamio.BuildConfig
import net.yuzumone.bergamio.MainApp
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.api.MioponClient
import net.yuzumone.bergamio.databinding.ActivityAppShortcutBinding
import net.yuzumone.bergamio.di.ActivityComponent
import net.yuzumone.bergamio.di.ActivityModule
import net.yuzumone.bergamio.fragment.ConfirmDialogFragment
import net.yuzumone.bergamio.model.HdoInfo
import net.yuzumone.bergamio.model.Toggle
import net.yuzumone.bergamio.model.ToggleCouponInfo
import net.yuzumone.bergamio.model.ToggleHdoInfo
import net.yuzumone.bergamio.util.PreferenceUtil

import javax.inject.Inject

class AppShortcutActivity : AppCompatActivity(), DialogInterface.OnDismissListener {

    private lateinit var binding: ActivityAppShortcutBinding
    private val number: String by lazy {
        intent.getStringExtra(ARG_NUMBER)
    }
    private val pref: PreferenceUtil by lazy {
        PreferenceUtil(this)
    }
    private var isCanceled = false
    @Inject lateinit var client: MioponClient
    @Inject lateinit var compositeDisposable: CompositeDisposable

    companion object {
        private const val ARG_NUMBER = "number"
        private const val CONFIRM_DIALOG_REQUEST = 4545
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
        if (PreferenceUtil(this).hasAvailableToken) {
            val dev = BuildConfig.DEVELOPER_ID
            val token = pref.token
            compositeDisposable.add(fetch(dev, token))
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CONFIRM_DIALOG_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) return
                    val hdoInfo = data
                            .getParcelableExtra<HdoInfo>(ConfirmDialogFragment.ARG_HDO_INFO)
                    val useCoupon = data
                            .getBooleanExtra(ConfirmDialogFragment.ARG_USE_COUPON, false)
                    val body = createBody(hdoInfo.hdoServiceCode, useCoupon)
                    val dev = BuildConfig.DEVELOPER_ID
                    val token = pref.token
                    compositeDisposable.add(putToggleCoupon(dev, token, body))
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    isCanceled = true
                }
            }
        }
    }

    private fun fetch(developer: String, token: String): Disposable {
        return client.getCoupon(developer, token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding.progress.visibility = View.VISIBLE }
                .doOnComplete { binding.progress.visibility = View.GONE }
                .subscribe (
                        { response ->
                            response.couponInfo.forEach coupon@ { couponInfo ->
                                couponInfo.hdoInfo.forEach { hdoInfo ->
                                    if (hdoInfo.number == number) {
                                        val fragment = ConfirmDialogFragment
                                                .newInstance(null, CONFIRM_DIALOG_REQUEST,
                                                        !hdoInfo.couponUse, hdoInfo)
                                        fragment.show(supportFragmentManager, "confirm")
                                        return@coupon
                                    }
                                }
                            }
                        },
                        { error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    private fun createBody(hdo: String, bool: Boolean): ToggleCouponInfo {
        val toggle = Toggle(hdo, bool)
        val hdoInfo = ToggleHdoInfo(arrayListOf(toggle))
        return ToggleCouponInfo(arrayListOf(hdoInfo))
    }

    private fun putToggleCoupon(developer: String, token: String, body: ToggleCouponInfo): Disposable {
        return client.putToggleCoupon(developer, token, body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding.progress.visibility = View.VISIBLE }
                .doOnComplete { binding.progress.visibility = View.GONE }
                .subscribe (
                        {
                            Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show()
                            finishAndRemoveTask()
                        },
                        { error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                            finishAndRemoveTask()
                        }
                )
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (isCanceled) finishAndRemoveTask()
    }

    override fun finishAndRemoveTask() {
        overridePendingTransition(0, 0)
        super.finishAndRemoveTask()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun getComponent(): ActivityComponent {
        val mainApplication = application as MainApp
        return mainApplication.appComponent.plus(ActivityModule(this))
    }
}