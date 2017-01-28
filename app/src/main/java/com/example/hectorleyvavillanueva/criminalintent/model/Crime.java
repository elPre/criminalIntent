package com.example.hectorleyvavillanueva.criminalintent.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hectorleyvavillanueva on 12/1/16.
 */

public class Crime implements Comparable<Crime>, Parcelable{

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mId);
        out.writeString(mTitle);
        out.writeSerializable(mDate);
        out.writeValue(mSolved);
        out.writeString(mSuspect);
    }

    public static final Parcelable.Creator<Crime> CREATOR
            = new Parcelable.Creator<Crime>() {
        public Crime createFromParcel(Parcel in) {
            return new Crime(in);
        }

        public Crime[] newArray(int size) {
            return new Crime[size];
        }
    };

    private Crime(Parcel in) {
        mId = (UUID) in.readSerializable();
        mTitle = in.readString();
        mDate = (Date)in.readSerializable();
        mSolved = (boolean) in.readValue(getmDate().getClass().getClassLoader());
        mSuspect = in.readString();
    }

    public Date getmDate() {
        return this.mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public String getPhotoFileName(){
        return "IMG_"+getmId().toString()+".jpg";
    }

    @Override
    public int compareTo(Crime o) {
        return this.getmId().compareTo(o.getmId());
    }


    public static class Builder{
        private UUID mId;
        private String mTitle;
        private Date mDate;
        private boolean mSolved;
        private String mSuspect;

        public Builder id(UUID id){
            this.mId = id;
            return this;
        }
        public Builder title(String title){
            this.mTitle = title;
            return this;
        }
        public Builder date(Date date){
            this.mDate = date;
            return this;
        }

        public Builder solved(boolean solved){
            this.mSolved = solved;
            return this;
        }

        public Builder suspect(String suspect){
            this.mSuspect = suspect;
            return this;
        }

        public Crime build(){
            return new Crime(this);
        }


    }

    private Crime(Builder builder){
        this.mId = builder.mId;
        this.mTitle = builder.mTitle;
        this.mDate = builder.mDate;
        this.mSolved = builder.mSolved;
        this.mSuspect = builder.mSuspect;
    }

}
