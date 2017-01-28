package com.example.hectorleyvavillanueva.criminalintent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.hectorleyvavillanueva.criminalintent.R;
import com.example.hectorleyvavillanueva.criminalintent.fragment.CrimeFragment;
import com.example.hectorleyvavillanueva.criminalintent.fragment.CrimeListFragment;
import com.example.hectorleyvavillanueva.criminalintent.model.Crime;

/**
 * Created by hectorleyvavillanueva on 12/2/16.
 */

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayouResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = CrimePagerActivity.newIntent(this, crime.getmId());
            startActivity(intent);
        }else{
            Fragment newDetail = CrimeFragment.newInstance(crime.getmId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
