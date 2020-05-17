package com.riyas.fxrate.view_model

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riyas.fxrate.R
import com.riyas.fxrate.model.AppConstants
import com.riyas.fxrate.model.FxRate
import com.wisilica.imsafe.logD
import com.wisilica.imsafe.logE
import com.wisilica.imsafe.logI
import com.wisilica.imsafe.logV
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.github.mikephil.charting.data.Entry


class FXViewModel(application: Application) : BaseViewModel(application) {

    private var mCurrenciesSymbols: String? = null

    var mCurrentMultiplier = ObservableField<BigDecimal>(1.0.toBigDecimal())

    private var mCurrencyComparatorList: ArrayList<FxRate>? = ArrayList<FxRate>()
    val mTodaysFxRateList = MutableLiveData<List<FxRate>>()
    var mCurrentHistoricData = MutableLiveData<HashMap<String, List<FxRate>?>?>()

    init {
        setCurrencyCodes()
    }


    fun getMultipliedValue(value: Double): String {
        val data = (value.toBigDecimal() * mCurrentMultiplier.get()!!) as BigDecimal
        return String.format("%.5f", data)
    }

    private fun setCurrencyCodes() {
        /*USD, EUR, JPY, GBP,
        AUD, CAD, CHF, CNY, SEK, NZD*/
        val builder = StringBuilder()
        builder.append("USD")
        builder.append(",")
        builder.append("EUR")
        builder.append(",")
        builder.append("JPY")
        builder.append(",")
        builder.append("GBP")
        builder.append(",")
        builder.append("AUD")
        builder.append(",")
        builder.append("CAD")
        builder.append(",")
        builder.append("CHF")
        builder.append(",")
        builder.append("CNY")
        builder.append(",")
        builder.append("SEK")
        builder.append(",")
        builder.append("NZD")
        mCurrenciesSymbols = builder.toString()

    }

    fun getTodaysRate() {

        if (!checkConnection(mApplication)) {
            mApiOnGoing.set(false)
            setState(AppConstants.NETWORK_FAILURE, mApplication.getString(R.string.network_failure))
            return
        }
        viewModelScope.launch(coroutineExceptionHandler) {

            if (mTodaysFxRateList?.value?.isNullOrEmpty() == true) {
                mApiOnGoing.set(true)
            } else {
                mApiOnGoing.set(false)

            }

            val response = cloudDataManager?.getTodaysRate(mCurrenciesSymbols)
            if (response?.isSuccess!!) {
                val map = response?.currencyRatesMap
                val list = getCurrencyRateList(map)
                mTodaysFxRateList.postValue(list)
            } else {
                if (response?.error != null) {
                    notifyError(AppConstants.API_ERROR, response?.error?.info)
                } else {
                    notifyError()
                }
            }
            mApiOnGoing.set(false)
        }

        // return mTodaysFxRateList
    }

    fun getLastFiveDaysRate(): MutableLiveData<HashMap<String, List<FxRate>?>?>? {

        val out = MutableLiveData<HashMap<String, List<FxRate>?>?>()

        if (!checkConnection(mApplication)) {
            mApiOnGoing.set(false)
            setState(AppConstants.NETWORK_FAILURE, mApplication.getString(R.string.network_failure))
            return out
        }
        viewModelScope.launch(coroutineExceptionHandler) {
            mApiOnGoing.set(true)
            val tempMap = HashMap<String, List<FxRate>?>()
            val symbols = getSelectedSymbols()
            val historicalDates = getHistoricalDates()
            for (date in historicalDates) {
                val response = cloudDataManager?.getHistoricalRate(date, symbols)
                if (response?.isSuccess!!) {
                    val map = response?.currencyRatesMap
                    val list = getCurrencyRateList(map)
                    tempMap[date] = list
                } else {

                    if (response?.error != null) {
                        notifyError(AppConstants.API_ERROR, response?.error?.info)
                    } else {
                        notifyError()
                    }
                }
            }
            out.postValue(tempMap)
            mApiOnGoing.set(false)
        }

        mCurrentHistoricData = out
        return out
    }

    private fun notifyError(
        code: Int = AppConstants.SOMETHING_WENT_WRONG,
        msg: String? = mApplication.getString(R.string.something_went_wrong)
    ) {
        mApiOnGoing.set(false)
        setState(
            code,
            msg
        )

    }

    private fun getHistoricalDates(): ArrayList<String> {

        val out = ArrayList<String>()
        for (i in 1 until 6) {
            val dayCounter = i * -1
            val date = getPreviousDate(i)
            val stringDate = getDateString(date)
            out.add(stringDate)
            logV(TAG, "getHistoricalDates(): dayCounter: $dayCounter => date=$stringDate")
        }
        return out

    }

    private fun getPreviousDate(noOfDays: Int): Date {
        return Date(System.currentTimeMillis() - noOfDays * 24 * 60 * 60 * 1000)
    }

    private fun getDateString(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(date)
    }

    private fun getSelectedSymbols(): String? {
        if (!mCurrencyComparatorList.isNullOrEmpty()) {
            var out = ""
            val size = mCurrencyComparatorList?.size!!
            for (i in 0 until size) {
                val rate = mCurrencyComparatorList!![i]
                out += rate.currencyCode
                if (i < size - 1) {
                    out += ","
                }
            }
            logV(TAG, "getSelectedSymbols() : $out")
            return out
        }
        return null
    }

    private fun getCurrencyRateList(map: Map<String, Double>?): List<FxRate>? {
        val list = ArrayList<FxRate>()
        if (!map.isNullOrEmpty()) {
            val keys = map.keys
            for (key in keys) {
                val rate = FxRate(key, map[key])
                list.add(rate)
            }
        }
        return list
    }

    fun hasAddedToCompareList(fxRate: FxRate): Boolean {
        return mCurrencyComparatorList?.contains(fxRate)!!
    }

    fun addOrRemoveCompareList(fxRate: FxRate): Boolean {
        if (fxRate.isSelected) {
            if (mCurrencyComparatorList.isNullOrEmpty()) {
                mCurrencyComparatorList = ArrayList<FxRate>()
            }

            val alreadyAdded=hasAddedToCompareList(fxRate)
            if (!alreadyAdded) {
                mCurrencyComparatorList?.add(fxRate)
                logI(TAG, "addOrRemoveCompareList() : Added Currency : ${fxRate.currencyCode}=>$alreadyAdded")
            }else{
                logI(TAG, "addOrRemoveCompareList() : Added Currency : ${fxRate.currencyCode}=>$alreadyAdded")

            }
        } else {
            val obj = mCurrencyComparatorList?.remove(fxRate)
            //if (obj!!) {
                logE(TAG, "addOrRemoveCompareList() : Removed Currency : ${fxRate.currencyCode}=>$obj")
            //}
        }
        if (mCurrencyComparatorList?.size!! >= 2) {
            logV(
                TAG,
                "addOrRemoveCompareList() : Currency Compare List Size : ${mCurrencyComparatorList?.size}"
            )
            return true
        }
        return false

    }

    fun clearCompareList() {
        if(mCurrencyComparatorList?.size!!>0){
            for(currency in mCurrencyComparatorList!!){
                currency.isSelected=false
            }
        }
        mCurrencyComparatorList?.clear()


    }

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        mApiOnGoing.set(false)
        exception.printStackTrace()
        logD(TAG, "Caught $exception")
        notifyError()
    }

    fun getXAxisValues(data: HashMap<String, List<FxRate>?>): ArrayList<String> {

        val xVals = ArrayList<String>()
        if (!data.isNullOrEmpty()) {
            val list = data.keys
            var tempList = list.toList()
            tempList = tempList.sorted()
            tempList = tempList.reversed()
            for (date in tempList) {
                xVals.add(date)
            }
        }
        return xVals
    }

    fun getYAxisValues(
        data: HashMap<String, List<FxRate>?>?,
        pos: Int
    ): Pair<String, ArrayList<Entry>> {

        val yVals = ArrayList<Entry>()
        var curreny = ""
        if (!data.isNullOrEmpty()) {
            val list = data.keys
            var tempList = list.toList()
            tempList = tempList.sorted()
            tempList = tempList.reversed()
            for (index in 1 until tempList.size + 1) {
                // xVals.add(date)
                val date = tempList[index - 1]
                val yList = data.get(date)
                if (!yList.isNullOrEmpty()) {
                    // for(index in 0 until  yList.size){
                    curreny = yList[pos]?.currencyCode!!
                    yVals.add(Entry(yList[pos]?.rate?.toFloat()!!, index - 1))
                    //}
                }


            }
        }
        return Pair(curreny, yVals)
    }
}