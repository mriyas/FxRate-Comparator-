package com.wisilica.imsafe

import android.util.Log
import com.riyas.fxrate.BuildConfig

fun logD(tag: String?, msg: String) {
    if (BuildConfig.DEBUG)
        Log.d(tag, msg)
}

fun logI(tag: String?, msg: String) {
    if (BuildConfig.DEBUG)
        Log.i(tag, msg)
}

fun logE(tag: String?, msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(tag, msg)
}

fun logW(tag: String?, msg: String) {
    if (BuildConfig.DEBUG)
        Log.w(tag, msg)
}

fun logV(tag: String?, msg: String) {
    if (BuildConfig.DEBUG)
        Log.v(tag, msg)
}