package com.example.hectorleyvavillanueva.criminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.hectorleyvavillanueva.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hectorleyvavillanueva on 12/7/16.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "extra_date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        Log.d(TAG,"DatePickerFragment newInstance(Date date)");
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG," Dialog onCreateDialog(Bundle savedInstanceState)");
        Date date = (Date)getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        Log.d(TAG,"this dude is hosted by :"+getActivity().getLocalClassName());

        mDatePicker = (DatePicker)view.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date){
        Log.d(TAG," void sendResult(int resultCode, Date date)");
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}
