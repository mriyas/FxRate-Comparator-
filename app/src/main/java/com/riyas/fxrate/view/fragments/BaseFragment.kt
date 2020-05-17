package com.riyas.fxrate.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.riyas.fxrate.R
import com.riyas.fxrate.model.AppConstants
import com.riyas.fxrate.view.receiver.ConnectivityReceiver
import com.riyas.fxrate.view_model.FXViewModel

/**
 * Base fragment class in which it observe the app states like connectivity changes and failure case
 * and this class will be used as the parent class of other Fragments
 * @see FxHistoricRateFragment
 * @see FxRateHomeFragment
 * @see AppState
 *
 *
 */
open abstract class BaseFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {
    val TAG = javaClass.simpleName
    val mViewModel: FXViewModel by activityViewModels()

    private var mAlert: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.mState?.observe(this, Observer {
            when (it.state) {
                AppConstants.NETWORK_FAILURE -> {
                    networkFailure()
                }
                AppConstants.SOMETHING_WENT_WRONG -> {
                    val msg = activity?.getString(R.string.something_went_wrong)
                    setMessage(msg)
                    showDialog(
                        msg,
                        activity?.getString(android.R.string.ok)
                    )
                }
                AppConstants.API_ERROR -> {
                    val msg = it?.msg
                    setMessage(msg)
                    showDialog(
                        msg,
                        activity?.getString(android.R.string.ok)
                    )
                }
            }
            mViewModel?.resetAppState()
        })
    }

    /**
     * shows snack bar with a message
     */
    fun showSnackBar(msg: String) {
        if (null == activity)
            return
        activity?.runOnUiThread {
            Snackbar.make(
                activity!!.window?.decorView!!.findViewById(android.R.id.content),
                msg,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Abstarct method to set UI message it might be error or warning message
     */
    abstract fun setMessage(error: String?, reverseCheck: Boolean = false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /**
     * Alerting network failure
     */
    fun networkFailure() {
        val msg = activity?.getString(R.string.network_failure)
        setMessage(msg)
        showDialog(
            msg,
            activity?.getString(android.R.string.ok)
        )
    }

    /**
     * Showing dialog with a message
     */
    fun showDialog(
        message: String?,
        okMsg: String?, clickListener: DialogInterface.OnClickListener? = null

    ) {
        dismissDialog()
        val dialogBuilder = AlertDialog.Builder(context!!)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton(okMsg, DialogInterface.OnClickListener { dialog, id ->
                clickListener?.onClick(dialog, id)
                dialog.dismiss()
            })
        // create dialog box
        mAlert = dialogBuilder.create()
        // set title for alert dialog box
        mAlert?.setTitle(activity?.getString(R.string.app_name))
        // show alert dialog
        mAlert?.show()
    }

    /**
     * Dismiss dialog if any
     */
    fun dismissDialog() {

        if (mAlert?.isShowing == true) {
            mAlert?.dismiss()
        }
    }

    /**
     * Shows bottom sheet dialog
     */
    protected fun showBottomDialog(bottomSheetFragment: BottomSheetDialogFragment) {
        if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.isCancelable = true
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.getTag())
            return
        }
    }
}