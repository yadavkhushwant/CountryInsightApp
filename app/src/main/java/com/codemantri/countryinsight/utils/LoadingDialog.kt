package com.codemantri.countryinsight.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.codemantri.countryinsight.R

object LoadingDialog {
    private lateinit var alertDialog: AlertDialog.Builder
    fun createDialog(context: Context, setCancellationOnTouch: Boolean): AlertDialog.Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false)
        alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(view)
        alertDialog.setCancelable(setCancellationOnTouch)
        return alertDialog
    }
}