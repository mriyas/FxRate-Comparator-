package com.riyas.fxrate.view.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.riyas.fxrate.R
import com.riyas.fxrate.view.fragments.BaseFragment
import com.riyas.fxrate.view.fragments.FxRateHomeFragment
import com.riyas.fxrate.view.receiver.ConnectivityReceiver
import com.riyas.fxrate.view_model.FXViewModel

/**
 * The activity class in which Navigation hosted
 */
class MainActivity : AppCompatActivity() {
    lateinit var mNavController: NavController
    internal var mDoubleBackToExitPressedOnce = false

    val mViewModel: FXViewModel by viewModels()
    val mReceiver = ConnectivityReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(mReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        setContentView(R.layout.activity_main)
        mNavController = findNavController(R.id.my_nav_host_fragment)
        val mTopToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mTopToolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    override fun onBackPressed() {
        val mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        val currentFragment = mNavHostFragment?.childFragmentManager?.fragments?.get(0)
        if (currentFragment is FxRateHomeFragment) {
            doubleClickDismiss(currentFragment)
            return
        } else {
            mViewModel.clearCompareList()
            super.onBackPressed()
        }

    }

    /**
     * double tap exit logic, a delay of 2 seconds to reset the flag to false
     */
    private fun doubleClickDismiss(fr: BaseFragment?) {
        if (mDoubleBackToExitPressedOnce) {
            finish()
            return
        }
        this.mDoubleBackToExitPressedOnce = true
        fr?.showSnackBar(getString(R.string.doubleClickExit))
        Handler().postDelayed({ mDoubleBackToExitPressedOnce = false }, 2000)

    }
}
