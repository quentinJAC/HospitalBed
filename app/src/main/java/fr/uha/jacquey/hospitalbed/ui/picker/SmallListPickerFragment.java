package fr.uha.jacquey.hospitalbed.ui.picker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public abstract class SmallListPickerFragment extends DialogFragment implements DialogInterface.OnClickListener {

    static public final String NAME = "name";

    protected abstract String getRequestKey ();

    protected abstract String getTitle ();

    protected abstract String [] buildInputs ();

    protected abstract String buildOutput (int position);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder .setTitle(getTitle())
                .setItems(buildInputs (), this)
                .setNegativeButton(android.R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String requestKey = getRequestKey();
        Bundle result = new Bundle();
        switch (which) {
        case DialogInterface.BUTTON_POSITIVE :
            break;
        case DialogInterface.BUTTON_NEGATIVE :
            result.putString(NAME, buildOutput(-1));
            break;
        case DialogInterface.BUTTON_NEUTRAL :
            break;
        default:
            result.putString(NAME, buildOutput(which));
            break;
        }
        getParentFragmentManager().setFragmentResult(requestKey, result);
    }
}
