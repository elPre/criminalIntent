package com.example.hectorleyvavillanueva.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hectorleyvavillanueva.criminalintent.R;
import com.example.hectorleyvavillanueva.criminalintent.activity.CrimePagerActivity;
import com.example.hectorleyvavillanueva.criminalintent.model.Crime;
import com.example.hectorleyvavillanueva.criminalintent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hectorleyvavillanueva on 12/2/16.
 */
public class CrimeListFragment extends Fragment {

    private SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("EEEE, MMM dd, yyyy");
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private final static String TAG = CrimeListFragment.class.getSimpleName();
    private int mPositionItem;
    private boolean mSubtitleVisible;
    private TextView mTextViewNoCrimes;
    private Callbacks mCallbacks;


    public interface Callbacks{
        void onCrimeSelected(Crime crime);
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime.Builder()
                        .date(new Date())
                        .id(UUID.randomUUID())
                        .build();
                CrimeLab.get(getActivity())
                        .addCrime(crime);
                //Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getmId());
                //startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) ");
        View v = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = (RecyclerView)v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextViewNoCrimes = (TextView) v.findViewById(R.id.fragment_crime_list_text_view);

        if(savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG," void onResume()");
        updateUI();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void updateUI(){
        Log.d(TAG," void updateUI()");
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> listCrimes = crimeLab.getCrimeList();
        if(mAdapter == null){
            mAdapter = new CrimeAdapter(listCrimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            //mAdapter.notifyItemChanged(mPositionItem);
            mAdapter.setCrimes(listCrimes);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG,"this would be the position "+mPositionItem);
        }
        updateSubtitle();
        showEmptyLabel();
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimeList().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plurals,crimeCount,crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }


    private void showEmptyLabel(){

        Log.d(TAG," void showEmptyLabel()");
        if(CrimeLab.get(getActivity()).getCrimeList().size() <= 0){
            mTextViewNoCrimes.setVisibility(View.VISIBLE);
            mCrimeRecyclerView.setVisibility(View.GONE);
        }else{
            mTextViewNoCrimes.setVisibility(View.GONE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        }

    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private final String TAG_ADAPTER = CrimeAdapter.class.getSimpleName();

        private List<Crime> mCrimeList;
        public CrimeAdapter(List<Crime> list) {
            mCrimeList = list;
        }


        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Log.d(TAG_ADAPTER, "CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) ");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);

            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Log.d(TAG_ADAPTER, "void onBindViewHolder(CrimeHolder holder, int position)");
            Crime crime = mCrimeList.get(position);
            holder.bindCrime(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimeList = crimes;
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final String TAG_HOLDER = CrimeHolder.class.getSimpleName();

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            Log.d(TAG_HOLDER,"CrimeHolder(View itemView)");
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_check_box);
            itemView.setOnClickListener(this);
        }

        public void bindCrime(Crime crime){
            Log.d(TAG_HOLDER,"bindCrime(Crime crime)");
            mCrime = crime;
            mTitleTextView.setText(mCrime.getmTitle());
            mDateTextView.setText(simpleDateFormat.format(mCrime.getmDate()));
            mSolvedCheckBox.setChecked(mCrime.ismSolved());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG_HOLDER,"void onClick(View v)");
            mPositionItem = getAdapterPosition();
            //Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getmId());
            //startActivity(intent);
            mCallbacks.onCrimeSelected(mCrime);
        }
    }

}
