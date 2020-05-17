package com.riyas.fxrate.model

import com.google.gson.annotations.SerializedName

class FixerApiResponse:BaseApiResponse() {
    @SerializedName("success")
    var isSuccess:Boolean=false
    @SerializedName("timestamp")
    var timestamp:Long=0L
    @SerializedName("base")
    var baseCurrencyCode:String?=null
    @SerializedName("date")
    var date:String?=null
    @SerializedName("rates")
    var currencyRatesMap:Map<String,Double>?=null


}