package com.sample.android.contact.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.Application
import com.sample.android.contact.R
import com.sample.android.contact.databinding.ActivitySplashBinding
import com.sample.android.contact.ui.contact.MainActivity
import com.sample.android.contact.util.Resource
import com.sample.android.contact.viewmodels.SplashViewModel
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: SplashViewModel.Factory

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, factory).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as Application).applicationComponent.inject(this)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val versionName = packageManager.getPackageInfoCompat(packageName).versionName
        binding.tvSplashAppVersion.text = getString(R.string.splash_app_version, versionName)

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    PERMISSIONS_REQUEST_PHONE_CALL
                )
            } else {
                checkContactAccessPermission()
            }
        } else {
            viewModel.loadContacts()
        }

        viewModel.liveData.observe(this) {
            if (it is Resource.Success) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkContactAccessPermission()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_phone_call_not_granted_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.loadContacts()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_contact_not_granted_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkContactAccessPermission() {
        if (checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            viewModel.loadContacts()
        }
    }
}

// Request code for READ_CONTACTS. It can be any number > 0.
private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
private const val PERMISSIONS_REQUEST_PHONE_CALL = 101

fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
} else {
    @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
}