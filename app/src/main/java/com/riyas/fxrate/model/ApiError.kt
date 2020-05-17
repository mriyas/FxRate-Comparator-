package com.riyas.fxrate.model

/**
 * Model class to hold API error
 * @param code: error code
 * @param info : error message
 */
data class ApiError(val code:Int,val info:String)