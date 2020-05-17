package com.riyas.fxrate.view.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.riyas.fxrate.R
import com.riyas.fxrate.view.fragments.FxRateHomeFragment
import com.riyas.fxrate.view.receiver.ConnectivityReceiver
import com.riyas.fxrate.view_model.FXViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mNavController: NavController

    val mViewModel: FXViewModel by viewModels ()
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
            finish()
            return
        }else{
            mViewModel.clearCompareList()
            super.onBackPressed()
        }

    }
}
