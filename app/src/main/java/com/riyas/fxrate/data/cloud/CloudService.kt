package com.riyas.fxrate.data.cloud

import android.content.Context
import com.riyas.fxrate.BuildConfig
import com.riyas.fxrate.model.FixerApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CloudService {

    /**
     * Fetch today's exchange rate
     * @param symbols : Comma Separated currency codes
     * @param accessKey : fixer access key
     * @param base : base currency code
     * @return FixerApiResponse class object
     * @see FixerApiResponse
     */

    @GET("latest")
    suspend fun getTodaysRate(@Query("access_key") accessKey: String,
                              @Query("base") baseCurrencyCode: String,
                              @Query("symbols") symbols: String?): FixerApiResponse
    /**
     * Fetch historical exchange rate
     * @param symbols : Comma Separated currency codes
     * @param date : date string in yyyy-MM-dd formatted
     * @param accessKey : fixer access key
     * @param base : base currency code
     * @return FixerApiResponse class object
     * @see FixerApiResponse
     */
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