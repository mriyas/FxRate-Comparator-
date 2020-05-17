package com.riyas.fxrate.view.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riyas.fxrate.R
import com.riyas.fxrate.model.AppConstants
import com.riyas.fxrate.view.adapters.FxRateHomeFragmentAdapter
import com.wisilica.imsafe.logI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.riyas.fxrate.databinding.FragmentFxRateHomeBinding
import com.riyas.fxrate.model.FxRate
import com.riyas.fxrate.view.receiver.ConnectivityReceiver

/**
 * Toady's exchange rate listing fragment
 */
class FxRateHomeFragment : BaseFragment() {
    var mAdapter: FxRateHomeFragmentAdapter? = null
    var mBinding: FragmentFxRateHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fx_rate_home,
            container,
            false
        ) as FragmentFxRateHomeBinding


        mBinding?.viewModel = mViewModel
        mBinding?.lifecycleOwner = viewLifecycleOwner
        mBinding?.fr = this
        // Inflate the layout for this fragment
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.rvMain?.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        mAdapter = FxRateHomeFragmentAdapter(mViewModel.mTodaysFxRateList.value, this)
        mAdapter?.setFooter(getString(R.string.enable_fx_selection_msg))
        mBinding?.rvMain?.adapter = mAdapter
        ConnectivityReceiver.connectivityReceiverListener = this
        mBinding?.etFx?.setText("${mViewModel.mCurrentMultiplier.get()}")

        observeData()

    }

    /**
     * Observes today's exchange rate api data
     */
    private fun observeData() {

        GlobalScope.launch(Dispatchers.Main) {
            mViewModel.mTodaysFxRateList.observe(viewLifecycleOwner, Observer {
                logI(TAG, "Observer Hit..." + it?.size)
                if (!isAdded) {
                    return@Observer
                }
                GlobalScope.launch(Dispatchers.Main) {
                    mAdapter?.setData(it)
                    setMsg()

                }
            })
            mViewModel.getTodaysRate()
            setMsg()
        }
    }
    /**
     * Sets UI error or warning messages
     * @param reverseCheck true for warnings false for errors
     */
    override fun setMessage(error: String?, reverseCheck:Boolean) {
        if (mAdapter?.itemCount!! <= 0) {
            mBinding?.progressBar2?.visibility = if(reverseCheck!!)View.VISIBLE else View.GONE
            mBinding?.tvMsg?.visibility = View.VISIBLE
            mBinding?.tvMsg?.text = error
        }else{
            mBinding?.progressBar2?.visibility =  View.GONE
            mBinding?.tvMsg?.visibility = View.GONE
        }
    }

    /**
     * Sets ui messages
     */
    private fun setMsg() {
        setMessage(getString(R.string.fetching_data),true)
        if (mAdapter?.itemCount!! > 0) {
            mBinding?.progressBar2?.visibility = View.GONE
            mBinding?.tvMsg?.visibility = View.GONE
            mBinding?.tvLabel?.visibility = View.VISIBLE
            mBinding?.etFx?.visibility = View.VISIBLE
            mBinding?.cbHistoricData?.visibility = View.VISIBLE
        } else {
            mBinding?.progressBar2?.visibility = View.VISIBLE
            mBinding?.tvMsg?.visibility = View.VISIBLE
            mBinding?.tvLabel?.visibility = View.GONE
            mBinding?.etFx?.visibility = View.GONE
            mBinding?.cbHistoricData?.visibility = View.GONE
        }
        mBinding?.cbHistoricData?.isChecked=false
    }

    /**
     * Text change watcher from data binding for multiplier of currency value
     */
     fun onTextChanged(s: Editable) {
        logI(TAG, "onTextChanged ${s.toString()}")
        if (!s.isNullOrEmpty()) {
            mViewModel.mCurrentMultiplier.set(s.toString().toBigDecimal())
        } else {
            mViewModel.mCurrentMultiplier.set(1.toBigDecimal())
        }
        mAdapter?.changeMultiplier()

    }

    /**
     * Historic check box button click
     */
    fun enableHistoricData(view: View) {
        if(view is CheckBox) {
            mAdapter?.enableHistoricData(view.isChecked)
            var msg=""
            if(view.isChecked){
                msg = getString(R.string.fx_selection_msg)

            }else{
               msg = getString(R.string.enable_fx_selection_msg)

            }
            mAdapter?.setFooter(msg)

        }

    }

    /**
     * Individual row check box button click
     */
    fun onClick(checkBox: View,fxRate: FxRate) {
        fxRate.isSelected = !fxRate.isSelected
        val state = mViewModel.addOrRemoveCompareList(fxRate)
        mAdapter?.notifyDataSetChanged()
        if (state) {
            mBinding?.cbHistoricData?.isChecked=false
            findNavController().navigate(R.id.goToHistoricRateHome)
        }
    }

    /**
     * listnes internet connectivity changes
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (mAdapter?.itemCount!! <= 0) {
            if(isConnected) {
                dismissDialog()
            }
            logI(TAG,"onNetworkConnectionChanged()....$isConnected")
            observeData()
        }
    }

}