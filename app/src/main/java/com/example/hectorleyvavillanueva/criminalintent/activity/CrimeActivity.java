package com.example.hectorleyvavillanueva.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.example.hectorleyvavillanueva.criminalintent.R;
import com.example.hectorleyvavillanueva.criminalintent.fragment.CrimeFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity{

    private static final String EXTRA_CRIME_ID = "crime_id";

    @Override
    protected Fragment createFragment() {

        return CrimeFragment.newInstance((UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID));
    }


}
