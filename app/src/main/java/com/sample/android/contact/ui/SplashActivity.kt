package com.sample.android.contact.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.sample.android.contact.R
import com.sample.android.contact.data.ContactsDataSource
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var dataSource: ContactsDataSource

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        tv_splash_app_version.text = getString(R.string.splash_app_version, versionName)

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else { // Android version is lesser than 6.0 or the permission is already granted.
            startMainActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission is granted
                startMainActivity()
            } else {
                Toast.makeText(this, getString(R.string.permission_not_granted_msg), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startMainActivity() {
        dataSource.loadAllContacts()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, SPLASH_DELAY.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}

private const val SPLASH_DELAY = 1500
// Request code for READ_CONTACTS. It can be any number > 0.
private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100