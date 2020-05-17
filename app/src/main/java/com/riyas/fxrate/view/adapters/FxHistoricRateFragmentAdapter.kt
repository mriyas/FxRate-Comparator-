package com.riyas.fxrate.view.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.riyas.fxrate.R
import com.riyas.fxrate.databinding.RvHistoricRatesBinding
import com.riyas.fxrate.databinding.RvHistoricRatesHeaderBinding
import com.riyas.fxrate.model.FxRate
import com.riyas.fxrate.view.fragments.FxHistoricRateFragment

/**
 * RecyclerView Adapter class which populated historic data
 */

class FxHistoricRateFragmentAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var TYPE_HEADER = 1
    var TYPE_ITEM = 0

    var fr: FxHistoricRateFragment? = null
    var mMultiplier: Double = 1.0

    var mMap:HashMap<String,List<FxRate>?>?=null
    var mData:List<String>?=null

    constructor( data:HashMap<String,List<FxRate>?>?,fr: FxHistoricRateFragment) : this() {
        this.fr = fr
        setData(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            TYPE_HEADER -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    inflater,
                    R.layout.rv_historic_rates_header,
                    parent,
                    false
                ) as RvHistoricRatesHeaderBinding
                return HistoricHeaderViewHolder(
                    binding
                )
            }
            else -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    inflater,
                    R.layout.rv_historic_rates,
                    parent,
                    false
                ) as RvHistoricRatesBinding
                return HistoricDataViewHolder(
                    binding
                )
            }
        }


    }

    /**
     * The first row will be used for header
     */
    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        }
        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        if (mData.isNullOrEmpty())
            return 0
        else
            return mData!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            val date=mData?.get(position)
            val list=mMap?.get(date)

            if (holder is HistoricHeaderViewHolder) {
                var currencyCode1="N/A"
                var currencyCode2="N/A"
                if(!list.isNullOrEmpty()&&list.size>=2){
                    currencyCode1=list[0].currencyCode!!
                    currencyCode2=list[1].currencyCode!!
                }
                holder.bind(currencyCode1,currencyCode2)
            }
        } else if (mData != null && position <= mData?.size!!) {
            val tempPos=position-1
            val date=mData?.get(tempPos)
            val list=mMap?.get(date)
            var rate1=0.0 as Double
            var rate2=0.0 as Double
            if(!list.isNullOrEmpty()&&list.size>=2){
                rate1=list[0].rate!!
                rate2=list[1].rate!!
            }
            if (holder is HistoricDataViewHolder) {
                holder.bind(tempPos,fr,date, rate1,rate2)
            }
        }
    }

    /**
     * This method will convert map into list and sort it in descending order and that list
     * will be used to populate recycler view
     */
    fun setData(it:HashMap<String,List<FxRate>?>?) {
        if(!it.isNullOrEmpty()) {
            this.mMap = it
            val dateList = mMap?.keys
            var dates=dateList?.toList()
            dates=dates?.sorted()
            dates=dates?.reversed()
            this.mData =dates
            notifyDataSetChanged()
        }
    }


    /**
     * Data View Holder class for historic data
     */
    class HistoricDataViewHolder(val binding: RvHistoricRatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the rates
         * @param rate1 : rate of currency code1
         * @param rate2 : rate of currency code2
         */
        fun bind(
            pos: Int,
            fr: FxHistoricRateFragment?,date:String?, rate1: Double, rate2: Double
        ) {
            binding.rate1 = rate1
            binding.rate2 = rate2
            binding.position = pos
            binding.date=date
            binding.fr = fr
            binding.executePendingBindings()
        }
    }

    /**
     * Header view holder class
     */
    class HistoricHeaderViewHolder(val binding: RvHistoricRatesHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            code1: String?, code2: String?
        ) {
            binding.code1 = code1
            binding.code2 = code2
            binding.executePendingBindings()
        }
    }


}
