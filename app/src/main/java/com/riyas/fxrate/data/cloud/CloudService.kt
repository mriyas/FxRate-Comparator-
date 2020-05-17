package com.riyas.fxrate.data.cloud

import android.content.Context
import com.riyas.fxrate.BuildConfig
import com.riyas.fxrate.model.FixerApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CloudService {



    @GET("latest")
    suspend fun getTodaysRate(@Query("access_key") accessKey: String,
                              @Query("base") baseCurrencyCode: String,
                              @Query("symbols") symbols: String?): FixerApiResponse
    @GET("{date}")
    suspend fun getHistoricalRate(@Path(value = "date", encoded = true) date: String?,
                                  @Query("access_key") accessKey: String,
                                  @Query("base") baseCurrencyCode: String,
                                  @Query("symbols") symbols: String?): FixerApiResponse


    companion object Factory {

        fun create(context: Context): CloudService? {
            val retrofit =RestClient.getClient(BuildConfig.WEB_SERVICE_BASE_URL)
            return retrofit
        }
    }
}