package com.christofer.atlas.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * @author Christoforos Nalmpantis
 *
 * This class is responsible for presenting error dialogs.
 */
public class ErrorDialogFragment extends DialogFragment {
    private Dialog dialog;

    /**
     * Creates a new {@link com.christofer.atlas.dialogs.ErrorDialogFragment}.
     */
    public ErrorDialogFragment(){
        super();
        dialog = null;
    }

    /**
     * Set the {@link android.app.Dialog} object.
     *
     * @param dialog
     */
    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.dialog;
    }

}
