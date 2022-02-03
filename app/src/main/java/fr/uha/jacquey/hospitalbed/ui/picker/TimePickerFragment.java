package fr.uha.jacquey.hospitalbed.ui.picker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    static public final String TIME = "time";
    private String requestKey;
    private Date initial;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TimePickerFragmentArgs arg = TimePickerFragmentArgs.fromBundle(getArguments());
        requestKey = arg.getRequestKey();
        initial = new Date(arg.getTime());
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(initial);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(getContext(), this, hour, minute, true);
        if (arg.getTitle() != 0) {
            dialog.setTitle(arg.getTitle());
        }
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(initial);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        long date = calendar.getTimeInMillis();
        Bundle result = new Bundle();
        result.putLong(TIME, date);
        getParentFragmentManager().setFragmentResult(requestKey, result);
    }

}
