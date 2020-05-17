package com.riyas.fxrate.view_model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.riyas.fxrate.data.cloud.CloudDataManager
import com.riyas.fxrate.model.AppState

open class BaseViewModel (application: Application):AndroidViewModel(application){
    val TAG=javaClass.simpleName
    var mApplication: Application
    val mState = MutableLiveData<AppState>()
    var cloudDataManager: CloudDataManager? = null
    var mApiOnGoing= ObservableField<Boolean>(false)
    init {
        this.mApplication=application
        cloudDataManager = getService()
    }

    fun getService(): CloudDataManager?{
        if (null == cloudDataManager)
            cloudDataManager = CloudDataManager(getApplication())
        return cloudDataManager
    }

    fun setState(status: Int,msg:String?=null) {
        var value=mState.value
        if(value==null){
            value=  AppState(msg)
        }
        val state=value
        state.state=status
        mState.postValue(state)
    }
    fun checkConnection(ctx: Context): Boolean {
        try {
            if (ctx != null) {
                val connMgr = ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connMgr.activeNetworkInfo
                return if (networkInfo != null && networkInfo.isConnected) {
                    true
                } else {
                    false
                }

            } else {
                return false
            }
        } catch (e: Exception) {
            return false
        }

    }

    fun resetAppState() {

        setState(-1,null)
    }
}