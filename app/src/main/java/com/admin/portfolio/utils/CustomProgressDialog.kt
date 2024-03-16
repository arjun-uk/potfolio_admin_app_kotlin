package com.admin.portfolio.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.admin.portfolio.R

class CustomProgressDialog(context: Context) : Dialog(context) {
    var progressBar: ProgressBar

    init {
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null)
        progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
        setContentView(dialogView)
        setCancelable(false)
    }

    fun showProgressDialog() {
        if (!isShowing) {
            show()
        }
    }

    fun hideProgressDialog() {
        if (isShowing) {
            dismiss()
        }
    }
}
