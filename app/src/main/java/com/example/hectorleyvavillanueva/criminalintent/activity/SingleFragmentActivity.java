package com.example.hectorleyvavillanueva.criminalintent.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.hectorleyvavillanueva.criminalintent.R;

/**
 * Created by hectorleyvavillanueva on 12/2/16.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = SingleFragmentActivity.class.getSimpleName();

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayouResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayouResId());

        Log.d(TAG,"void onCreate(Bundle savedInstanceState) ");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }

    }

}
