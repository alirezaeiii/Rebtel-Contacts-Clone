package com.sample.android.contact.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.sample.android.contact.ContactsService
import com.sample.android.contact.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startService(Intent(applicationContext, ContactsService::class.java))
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        tv_splash_app_version.text = getString(R.string.splash_app_version, versionName)
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, SPLASH_DELAY.toLong())
    }
}

private const val SPLASH_DELAY = 1500