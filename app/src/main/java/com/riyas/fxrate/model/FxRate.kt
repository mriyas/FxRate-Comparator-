package com.riyas.fxrate.model

/**
 * A model class which holds currency details like currency code and it rate
 * @param currencyCode : currency code
 * @param rate : rate of FX
 *
 */
data class FxRate(val currencyCode:String?, val rate:Double?){
    /**
     * Flag to identify the selection while adding to compare the rates
     */
    var isSelected=false
}