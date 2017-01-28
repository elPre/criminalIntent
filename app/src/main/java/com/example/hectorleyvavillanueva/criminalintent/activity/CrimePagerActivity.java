package com.example.hectorleyvavillanueva.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.hectorleyvavillanueva.criminalintent.R;
import com.example.hectorleyvavillanueva.criminalintent.fragment.CrimeFragment;
import com.example.hectorleyvavillanueva.criminalintent.model.Crime;
import com.example.hectorleyvavillanueva.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;


/**
 * Created by hectorleyvavillanueva on 12/6/16.
 */

public class CrimePagerActivity extends AppCompatActivity
        implements CrimeFragment.Callbacks{

    private static final String TAG = CrimePagerActivity.class.getSimpleName();
    private static final String EXTRA_CRIME_ID = "crime_id";
    private static final String EXTRA_ITEM_POSITION = "extraItemPosition";

    private ViewPager mViewPager;
    private List<Crime> mListCrime;

    public static Intent newIntent(Context packageManager, UUID crimeId) {
        Intent intent = new Intent(packageManager, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        //intent.putExtra(EXTRA_ITEM_POSITION,itemPosition);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        Log.d(TAG, "void onCreate(Bundle savedInstanceState)");


        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        final int itemPositionRV = getIntent().getIntExtra(EXTRA_ITEM_POSITION,-1);

        Log.d(TAG,"this is the value of the RV item position: "+itemPositionRV);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mListCrime = CrimeLab.get(this).getCrimeList();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(mListCrime.get(position).getmId());
            }

            @Override
            public int getCount() {
                return mListCrime.size();
            }
        });


        for (int i = 0; i < mListCrime.size(); i++) {
            if (crimeId.equals(mListCrime.get(i).getmId())) {
                mViewPager.setCurrentItem(i);
                Log.d(TAG,"position inside the list: "+i);
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d(TAG, "onPageScrolled position:" + position+" --positionOffset:"+positionOffset+" --positionOffsetPixels:"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.d(TAG, "onPageScrollStateChanged " + state);
            }
        });


    }



    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
