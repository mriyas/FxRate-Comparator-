package com.riyas.fxrate.data.cloud

import android.content.Context
import com.riyas.fxrate.BuildConfig
import com.riyas.fxrate.model.FixerApiResponse

class CloudDataManager (var context: Context){
    val service = CloudService.create(context)

    val TAG=javaClass.simpleName

    val accessKey=BuildConfig.ACCESS_KEY
    val base="EUR"



    suspend fun getTodaysRate(symbols:String?): FixerApiResponse? {
        return service?.getTodaysRate(accessKey,base,symbols)
    }

    suspend fun getHistoricalRate(date:String?,symbols:String?): FixerApiResponse? {
        return service?.getHistoricalRate(date,accessKey,base,symbols)
    }
}