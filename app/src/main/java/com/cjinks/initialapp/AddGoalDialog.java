package com.cjinks.initialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class AddGoalDialog extends DialogFragment {
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String goal);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    NoticeDialogListener listener;
    View dialogView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        EditText editText = (EditText) dialogView.findViewById(R.id.newgoal);
        builder.setPositiveButton(R.string.add_goal_dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String str = editText.getText().toString();
                        if(str != null) {
                            listener.onDialogPositiveClick(AddGoalDialog.this, str);
                        }
                    }
                });
        builder.setNegativeButton(R.string.add_goal_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(AddGoalDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Activity must implement NoticeDialogListener");
        }
    }
}
