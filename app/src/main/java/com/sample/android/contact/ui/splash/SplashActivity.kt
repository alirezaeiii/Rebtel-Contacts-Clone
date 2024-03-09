package com.sample.android.contact.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        binding.tvSplashAppVersion.text = getString(R.string.splash_app_version, versionName)

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else { // Android version is less than 6.0 or the permission is already granted.
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission is granted
                viewModel.loadContacts()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_granted_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

// Request code for READ_CONTACTS. It can be any number > 0.
private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100