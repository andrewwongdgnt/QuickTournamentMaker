package com.dgnt.quickTournamentMaker.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.dgnt.quickTournamentMaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


object AlertUtil {

    fun showError(
        context: Context,
        @StringRes
        baseErrorMessageResource: Int?,
        error: Exception
    ) = context.run {
        MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
            .setTitle(getString(R.string.error))
            .setMessage(getString(baseErrorMessageResource ?: R.string.genericErrorMsg, error.localizedMessage))
            .setNegativeButton(R.string.dismiss, null)
            .setPositiveButton(R.string.reportError) { _, _ ->
                try {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("andrew.wong.dgnt@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.errorEmailSubject))
                        putExtra(Intent.EXTRA_TEXT, getString(R.string.errorEmailBody, error.stackTraceToString()))
                        data = Uri.parse("mailto:")
                    }, getString(R.string.emailPickerTitle)))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, R.string.noEmailClientMessage, Toast.LENGTH_LONG).show()
                }
            }
            .create()
            .show()
    }

}