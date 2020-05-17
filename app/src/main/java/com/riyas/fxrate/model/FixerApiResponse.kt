package com.riyas.fxrate.model

import com.google.gson.annotations.SerializedName

/**
 * Model class holds the information from the API response
 */
class FixerApiResponse:BaseApiResponse() {
    /**
     * Flag to identify api is success or not
     */
    @SerializedName("success")
    var isSuccess:Boolean=false
    /**
     * Timestamp
     */
    @SerializedName("timestamp")
    var timestamp:Long=0L
    /**
     * Base currency code
     */
    @SerializedName("base")
    var baseCurrencyCode:String?=null
    /**
     * Formatted date string
     */
    @SerializedName("date")
    var date:String?=null
    /**
     * Map contains currency code and respective rate based on base currency
     */
    @SerializedName("rates")
    var currencyRatesMap:Map<String,Double>?=null


}