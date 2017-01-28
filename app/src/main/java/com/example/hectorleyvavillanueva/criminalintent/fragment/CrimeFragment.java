package com.example.hectorleyvavillanueva.criminalintent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hectorleyvavillanueva.criminalintent.R;
import com.example.hectorleyvavillanueva.criminalintent.model.Crime;
import com.example.hectorleyvavillanueva.criminalintent.model.CrimeLab;
import com.example.hectorleyvavillanueva.criminalintent.model.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hectorleyvavillanueva on 12/1/16.
 */

public class CrimeFragment extends DialogFragment {

    private static final String TAG = CrimeFragment.class.getSimpleName();

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DELETE_DIALOG = "DeleteDialog";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_DELETE = 1;
    private static final String ITEM_POSITION = "itemPosition";
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
    private int mPosition;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Callbacks mCallbacks;


    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }

    public static CrimeFragment newInstance(UUID uuid) {
        Log.d(TAG, "CrimeFragment newInstance(UUID uuid)");
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, uuid);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallbacks = (Callbacks) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "void onCreate(@Nullable Bundle savedInstanceState)");
        setHasOptionsMenu(true);
        UUID uuid = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(uuid);
        mPosition = getArguments().getInt(ITEM_POSITION);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_crime_fragment_delete_crime:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteDialog() {
        FragmentManager manager = getFragmentManager();
        DeleteDialogFragment dialog = DeleteDialogFragment.newInstance(mCrime,mPosition);
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DELETE);
        dialog.show(manager, DELETE_DIALOG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container\n" +
                "            , @Nullable Bundle savedInstanceState)");
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mDateButton = (Button) v.findViewById(R.id.crime_date_btn);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solve_checkbox);
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mPhotoButton = (ImageButton) v.findViewById(R.id.fragment_crime_image_button);
        mPhotoView = (ImageView) v.findViewById(R.id.fragment_crime_image_view);


        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mDateButton.setOnClickListener(new View.OnClickListener() ->void onClick(View v)");
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "mTitleField.addTextChangedListener(new TextWatcher() ->void onTextChanged(CharSequence s, int start, int before, int count) ");
                mCrime.setmTitle(s.toString());
                updateCrime(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() -> void onCheckedChanged(CompoundButton buttonView, boolean isChecked)");
                mCrime.setmSolved(isChecked);
                updateCrime(false);
            }
        });


        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getmSuspect() != null){
            mSuspectButton.setText(mCrime.getmSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
            mSuspectButton.setEnabled(false);



        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });



        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "void onActivityResult(int requestCode, int resultCode, Intent data)");
        if (resultCode != Activity.RESULT_OK)
            return;
        else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateCrime(false);
            updateDate();

        }
        else {
            if (requestCode == REQUEST_DELETE) {
                Crime crime = data.getParcelableExtra(DeleteDialogFragment.EXTRA_CRIME);
                CrimeLab.get(getActivity()).deleteCrime(crime);
                updateCrime(true);
                getActivity().finish();
            } else if (requestCode == REQUEST_CONTACT) {
                Uri contactUri = data.getData();
                //specify which fields you want your query to return values for.
                String[] queryFields = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                //Perform your query - the contractUri is like a "where" clause here
                Cursor c = getActivity().getContentResolver()
                        .query(contactUri, queryFields, null, null, null);
                try {
                    //Double-check that the results were got
                    if (c.getCount() == 0)
                        return;

                    //Pull out the first column of the first row of the data
                    //that is your suspect's name
                    c.moveToFirst();
                    String suspect = c.getString(0);
                    updateCrime(false);
                    mCrime.setmSuspect(suspect);
                    mSuspectButton.setText(mCrime.getmSuspect());
                } finally {
                    if (c != null){
                        c.close();
                    }else{
                        throw new NullPointerException("the cursor is null");
                    }
                }
            } else if (requestCode == REQUEST_PHOTO) {
                updateCrime(false);
                updatePhotoView();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void updateDate() {
        Log.d(TAG, "void updateDate()");
        mDateButton.setText(simpleDateFormat.format(mCrime.getmDate()));
    }

    private String getCrimeReport(){
        String solvedString;
        if(mCrime.ismSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getmDate()).toString();

        String suspect = mCrime.getmSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report, mCrime.getmTitle()
                , dateString, solvedString, suspect);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void updateCrime(boolean isDelete) {
        if(!isDelete){
            CrimeLab.get(getActivity()).updateCrime(mCrime);
        }
        mCallbacks.onCrimeUpdated(mCrime);
    }

}
