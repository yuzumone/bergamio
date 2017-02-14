package net.yuzumone.bergamio.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import net.yuzumone.bergamio.BuildConfig
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.databinding.ActivityAuthBinding
import net.yuzumone.bergamio.util.PreferenceUtil
import java.util.regex.Pattern

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        val clientId = BuildConfig.DEVELOPER_ID
        val redirect = BuildConfig.REDIRECT_URI
        val uri = "https://api.iijmio.jp/mobile/d/v1/authorization/" +
                "?response_type=token&state=state" +
                "&client_id=$clientId" + "&redirect_uri=$redirect"
        binding.buttonAuth.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null || intent.data == null) {
            return
        }
        val fragment = intent.data.fragment
        val token = getToken(fragment)
        val expire = getExpire(fragment)
        if (token != "" && expire != "") {
            PreferenceUtil.storeToken(this, token)
            PreferenceUtil.storeExpire(this, expire)
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
        } else {
            Toast.makeText(this, getString(R.string.auth_miss), Toast.LENGTH_LONG).show()
        }
    }

    private fun getToken(string: String): String {
        val p = Pattern.compile("(access_token=)(\\w+)")
        val m = p.matcher(string)
        if (m.find()) {
            return m.group(2)
        } else {
            return ""
        }
    }

    private fun getExpire(string: String): String {
        val p = Pattern.compile("(expires_in=)(\\d+)")
        val m = p.matcher(string)
        if (m.find()) {
            return m.group(2)
        } else {
            return ""
        }
    }
}