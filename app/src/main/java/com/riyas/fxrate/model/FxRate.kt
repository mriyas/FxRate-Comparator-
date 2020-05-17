package com.riyas.fxrate.model

/**
 * A model class which holds currency details like currency code and it rate
 */
data class FxRate(val currencyCode:String?, val rate:Double?){
    var isSelected=false
}