package com.riyas.fxrate.data.cloud

import com.riyas.fxrate.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit APi client class
 */
object RestClient {

    private var mRestService: CloudService? = null
    /**
     * Returns dao to get data from API
     */
    fun getClient(url: String): CloudService? {
        if (mRestService == null) {
            val builder = OkHttpClient().newBuilder()
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
            }
            val client = builder.build()

            val retrofit = Retrofit.Builder()
                // Using custom Jackson Converter to parse JSON
                // Add dependencies:
                // com.squareup.retrofit:converter-jackson:2.0.0-beta2
                .addConverterFactory(GsonConverterFactory.create())
                // Endpoint
                .baseUrl(url)
                .client(client)
                .build()

            mRestService = retrofit.create(CloudService::class.java)
        }
        return mRestService
    }
}
