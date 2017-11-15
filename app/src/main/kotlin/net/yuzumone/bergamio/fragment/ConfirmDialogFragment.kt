package net.yuzumone.bergamio.fragment

import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import net.yuzumone.bergamio.R
import net.yuzumone.bergamio.model.HdoInfo

class ConfirmDialogFragment : DialogFragment() {

    private val useCoupon by lazy {
        arguments.getBoolean(ARG_USE_COUPON)
    }
    private val hdoInfo: HdoInfo by lazy {
        arguments.getParcelable<HdoInfo>(ARG_HDO_INFO)
    }

    companion object {
        const val ARG_USE_COUPON = "use_coupon"
        const val ARG_HDO_INFO = "hdo_info"
        fun newInstance(fragment: Fragment, request: Int, useCoupon: Boolean, hdoInfo: HdoInfo): ConfirmDialogFragment {
            return ConfirmDialogFragment().apply {
                setTargetFragment(fragment, request)
                arguments = Bundle().apply {
                    putBoolean(ARG_USE_COUPON, useCoupon)
                    putParcelable(ARG_HDO_INFO, hdoInfo)
                }
            }
        }
    }

    private fun toggleCoupon() {
        val result = Intent()
        result.putExtra(ARG_HDO_INFO, hdoInfo)
        result.putExtra(ARG_USE_COUPON, useCoupon)
        if (targetFragment != null) {
            targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, result)
        } else {
            val pendingResult = activity.createPendingResult(targetRequestCode, result,
                    PendingIntent.FLAG_ONE_SHOT)
            try {
                pendingResult.send(Activity.RESULT_OK)
            } catch (e: PendingIntent.CanceledException) {
                e.printStackTrace()
            }
        }
    }

    private fun cancel() {
        if (targetFragment != null) {
            targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
        } else {
            val pendingResult = activity.createPendingResult(targetRequestCode, null,
                    PendingIntent.FLAG_ONE_SHOT)
            try {
                pendingResult.send(Activity.RESULT_CANCELED)
            } catch (e: PendingIntent.CanceledException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = if (useCoupon) getString(R.string.confirm_on) else getString(R.string.confirm_off)
        return AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    toggleCoupon()
                }
                .setNegativeButton("CANCEL") { dialog, which ->
                    cancel()
                }
                .create()
    }
}