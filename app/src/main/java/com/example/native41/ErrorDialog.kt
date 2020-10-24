package com.example.native41

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ErrorDialog : DialogFragment() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val args by navArgs<ErrorDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        logger.info("onCreateDialog")
        return AlertDialog.Builder(requireActivity()).apply {
            setTitle(args.titleId)
            if (args.messageArgs.isEmpty()) {
                setMessage(args.messageId)
            } else {
                setMessage(getString(args.messageId, *(args.messageArgs)))
            }
            setPositiveButton(android.R.string.ok) { dialog, which ->
                logger.info("OnClick $which")
                dialog.dismiss()
            }
        }.create()
    }

}