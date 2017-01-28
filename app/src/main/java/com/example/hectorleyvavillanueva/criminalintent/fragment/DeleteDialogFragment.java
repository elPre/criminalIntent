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

import com.example.hectorleyvavillanueva.criminalintent.R;
import com.example.hectorleyvavillanueva.criminalintent.model.Crime;

/**
 * Created by hectorleyvavillanueva on 12/13/16.
 */

public class DeleteDialogFragment extends DialogFragment {


    private static final String TAG = DeleteDialogFragment.class.getSimpleName();
    private static final String CRIME_TO_DELETE = "crimeToDelete";
    private static final String POSTION_ITEM = "positionItemDelete";
    public static final String EXTRA_CRIME = "extraCrime";



    public static DeleteDialogFragment newInstance(Crime crime, int position) {
        Bundle args = new Bundle();
        args.putParcelable(CRIME_TO_DELETE, crime);
        args.putInt(POSTION_ITEM,position);

        DeleteDialogFragment fragment = new DeleteDialogFragment();
        fragment.setArguments(args);
        Log.d(TAG,"DeleteDialogFragment newInstance(Crime crime)");
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_delete_crime, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Crime crime = getArguments().getParcelable(CRIME_TO_DELETE);
                        int position = getArguments().getInt(POSTION_ITEM);
                        sendResult(Activity.RESULT_OK,crime,position);

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();

    }

    private void sendResult(int resultCode, Crime crime, int position){
        Log.d(TAG," void sendResult(int resultCode, Crime crime)");
        if(getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CRIME,crime);
        intent.putExtra(POSTION_ITEM,position);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}

