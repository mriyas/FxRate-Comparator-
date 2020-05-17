package com.riyas.fxrate.data.cloud

import android.content.Context
import com.riyas.fxrate.BuildConfig
import com.riyas.fxrate.model.FixerApiResponse

/**
 * Cloud data manager class in which it will communicate to network layer using Retrofit API
 */
class CloudDataManager (var context: Context){
    val service = CloudService.create(context)

    val TAG=javaClass.simpleName

    val accessKey=BuildConfig.ACCESS_KEY
    val base="EUR"


    /**
     * Fetch today's exchange rate
     * @param symbols : Comma Separated currency codes
     * @return FixerApiResponse class object
     * @see FixerApiResponse
     */
    suspend fun getTodaysRate(symbols:String?): FixerApiResponse? {
        return service?.getTodaysRate(accessKey,base,symbols)
    }
    /**
     * Fetch historical exchange rate
     * @param symbols : Comma Separated currency codes
     * @param date : date string in yyyy-MM-dd formatted
     * @return FixerApiResponse class object
     * @see FixerApiResponse
     */
    suspend fun getHistoricalRate(date:String?,symbols:String?): FixerApiResponse? {
        return service?.getHistoricalRate(date,accessKey,base,symbols)
    }
}