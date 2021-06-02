package com.intern.spacex;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

public class ConfirmationDialog {
    public static AlertDialog getDialog(Activity activity, DeleteConfirmation listener) {
        String msz = "   Delete all data?";
        AlertDialog alertDialog =  new AlertDialog.Builder(activity)
                .setTitle("Delete")
                .setMessage(msz)
                .setIcon(R.drawable.ic_baseline_delete_24)
                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    listener.onDelete();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
        return alertDialog;
    }
}
