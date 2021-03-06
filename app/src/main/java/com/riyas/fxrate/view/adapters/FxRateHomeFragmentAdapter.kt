package com.riyas.fxrate.view.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.riyas.fxrate.R
import com.riyas.fxrate.databinding.RvFooterTextBinding
import com.riyas.fxrate.databinding.RvFxRatesBinding
import com.riyas.fxrate.model.FxRate
import com.riyas.fxrate.view.fragments.FxRateHomeFragment

/**
 * Recycler Adapter class for Toady's exchange rate
 */

class FxRateHomeFragmentAdapter(var mData: List<FxRate>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private  var TYPE_FOOTER = 1
    private var TYPE_ITEM = 0

    private var fr: FxRateHomeFragment? = null

    private var mFooterText:String?=null

    private var enableHistoricData:Boolean=false

    constructor(deviceList: List<FxRate>?, fr: FxRateHomeFragment) : this(deviceList) {
        mData = deviceList
        this.fr = fr
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            TYPE_FOOTER -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    inflater,
                    R.layout.rv_footer_text,
                    parent,
                    false
                ) as RvFooterTextBinding
                return FxRateFooterViewHolder(
                    binding
                )
            }
            else -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    inflater,
                    R.layout.rv_fx_rates,
                    parent,
                    false
                ) as RvFxRatesBinding
                return FxRateViewHolder(
                    binding
                )
            }
        }


    }

    /**
     * Last row will be used for footer to add some hint the user to enable the historic data
     * or to select 2 rows for comparison
     */
    override fun getItemViewType(position: Int): Int {
        if (position == mData?.size) {
            return TYPE_FOOTER
        }
        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        if (null == mData)
            return 0
        else
            return mData!!.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FxRateFooterViewHolder) {
            holder.bind(mFooterText)
        }else if (mData != null && position < mData?.size!!) {
            val current = mData!![position]

            if (holder is FxRateViewHolder) {
                holder.bind(current, position, fr,enableHistoricData)
            }
        }
    }

    /**
     * Sets data to populate recycler view
     */
    fun setData(it: List<FxRate>?) {
        mData = it
        notifyDataSetChanged()
    }

    /**
     * Changes the multiplication factor and the data will be recalculated based on the multiplier
     * by data binding
     */
    fun changeMultiplier() {

        notifyDataSetChanged()
    }

    /**
     * Sets flag to enable historic data comparison, this flag will determine whether to show individual
     * row checkboxes
     */
    fun enableHistoricData(checked: Boolean) {
        this.enableHistoricData=checked
        notifyDataSetChanged()
    }

    /**
     * Sets/changes footer texts
     */
    fun setFooter(msg: String) {
        this.mFooterText=msg
        notifyDataSetChanged()
    }

    /**
     * View holder class for data
     */

    class FxRateViewHolder(val binding: RvFxRatesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            dataModel: FxRate, pos: Int,
            fr: FxRateHomeFragment?,isCheck:Boolean=false
        ) {
            binding.data = dataModel
            binding.fr = fr
            binding.enabledHistoricData=isCheck
            //binding.multiplier = value
            binding.position = pos
            binding.executePendingBindings()
        }
    }

    /**
     * Footer view holder class
     */
    class FxRateFooterViewHolder(val binding: RvFooterTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            footerText: String?
        ) {

            binding.footerText = footerText
            binding.executePendingBindings()
        }
    }


}
