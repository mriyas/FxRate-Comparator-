package com.riyas.fxrate.view.receiver
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest



class ConnectivityReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

       /* val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        cm!!.requestNetwork(request,
            object : ConnectivityManager.NetworkCallback() {
                 override fun onAvailable(network: Network) {
                    // Do something once the network is available
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener!!.onNetworkConnectionChanged(true)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener!!.onNetworkConnectionChanged(false)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)

                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener!!.onNetworkConnectionChanged(false)
                    }                }
            })*/
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnectedOrConnecting(context!!))
        }



    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}