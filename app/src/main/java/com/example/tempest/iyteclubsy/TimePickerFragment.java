package com.example.tempest.iyteclubsy;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Bora Gültekin on 8.05.2018.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView time = (TextView) getActivity().findViewById(R.id.time);
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hourOfDay, minute);
        java.text.DateFormat dateTimeInstance = new SimpleDateFormat("HH:mm");
        String formattedDate = dateTimeInstance.format(calendar.getTime());
//        formattedDate = formattedDate.substring(0, formattedDate.length() - 6) + " " + formattedDate.substring(formattedDate.length() - 2, formattedDate.length());
        time.setText(formattedDate);
    }

}
