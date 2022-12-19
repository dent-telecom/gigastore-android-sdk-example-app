package com.dentwireless.gigastore_example_app.ui

import android.app.AlertDialog
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    // region Properties

    protected var activeError: Error? = null
        set(value) {
            field = value
            if (value != null) {
                showErrorDialog(value)
            }
        }

    // endregion

    // region Public API

    protected fun showErrorDialog(error: Error) {
        val currentContext = context ?: return
        val builder = AlertDialog.Builder(currentContext)
            .setMessage(error.message)
            .setNeutralButton("OK") { dialog, _ ->
                activeError = null
                dialog?.dismiss()
            }
        builder.show()
    }

    // endregion
}