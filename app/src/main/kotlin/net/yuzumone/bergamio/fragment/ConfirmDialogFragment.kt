package net.yuzumone.bergamio.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import net.yuzumone.bergamio.R

class ConfirmDialogFragment : DialogFragment() {

    private lateinit var listener: OnConfirmListener
    private var bool: Boolean = true

    interface OnConfirmListener {
        fun onToggleCoupon(bool: Boolean)
    }

    companion object {
        val ARG_BOOL = "bool"
        fun newInstance(fragment: Fragment, bool: Boolean): ConfirmDialogFragment {
            return ConfirmDialogFragment().apply {
                setTargetFragment(fragment, 0)
                arguments = Bundle().apply {
                    putBoolean(ARG_BOOL, bool)
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = targetFragment as OnConfirmListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Don't implement Listener.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            bool = arguments.getBoolean(ARG_BOOL)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = if (bool) getString(R.string.confirm_on) else getString(R.string.confirm_off)
        val dialog =  AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    listener.onToggleCoupon(bool)
                    dismiss()
                }
                .setNegativeButton("CANCEL") { dialog, which -> dismiss()}
                .create()
        return dialog
    }
}