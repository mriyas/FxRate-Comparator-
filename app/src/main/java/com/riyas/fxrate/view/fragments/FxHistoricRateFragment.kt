package com.riyas.fxrate.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riyas.fxrate.R
import com.riyas.fxrate.databinding.FragmentFxRateHistoryBinding
import com.riyas.fxrate.databinding.FragmentFxRateHomeBinding
import com.riyas.fxrate.view.adapters.FxHistoricRateFragmentAdapter
import com.riyas.fxrate.view.receiver.ConnectivityReceiver
import com.wisilica.imsafe.logI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FxHistoricRateFragment : BaseFragment() {

    var mAdapter: FxHistoricRateFragmentAdapter? = null

    var mBinding: FragmentFxRateHistoryBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_fx_rate_history,
            container,
            false
        ) as FragmentFxRateHistoryBinding


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
        mAdapter = FxHistoricRateFragmentAdapter(mViewModel.getLastFiveDaysRate()?.value, this)
        mBinding?.rvMain?.adapter = mAdapter
        mBinding?.tvFx?.text = mViewModel.mCurrentMultiplier.get().toString()
        ConnectivityReceiver.connectivityReceiverListener = this

        observeData()

    }

    override fun setMessage(error: String?, reverseCheck: Boolean) {
        if (mAdapter?.itemCount!! <= 0) {
            mBinding?.progressBar2?.visibility = if (reverseCheck!!) View.VISIBLE else View.GONE
            mBinding?.tvMsg?.visibility = View.VISIBLE
            mBinding?.tvMsg?.text = error
        } else {
            mBinding?.progressBar2?.visibility = View.GONE
            mBinding?.tvMsg?.visibility = View.GONE
        }
    }

    fun onBack() {
        activity?.onBackPressed()
    }

    private fun observeData() {
        GlobalScope.launch(Dispatchers.Main) {
            mViewModel.getLastFiveDaysRate()?.observe(viewLifecycleOwner, Observer {
                if (!isAdded) {
                    return@Observer
                }
                GlobalScope.launch(Dispatchers.Main) {
                    logI(TAG, "Observer Hit..." + it?.size)
                    mAdapter?.setData(it)

                }
            })
            setMessage(getString(R.string.fetching_data), true)
        }
    }

    fun showGraph() {

        if (!mViewModel.mCurrentHistoricData.value.isNullOrEmpty()) {
            showBottomDialog(GraphBottomSheetFragment())
        } else {
            showSnackBar(getString(R.string.data_not_available))
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (mAdapter?.itemCount!! <= 0) {
            if (isConnected) {
                dismissDialog()
            }
            logI(TAG, "onNetworkConnectionChanged()....$isConnected")
            observeData()
        }
    }


}