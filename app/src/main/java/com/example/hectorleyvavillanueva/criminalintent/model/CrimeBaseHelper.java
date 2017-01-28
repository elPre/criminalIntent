package com.example.hectorleyvavillanueva.criminalintent.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hectorleyvavillanueva.criminalintent.model.CrimeDbSchema.CrimeTable;

/**
 * Created by hectorleyvavillanueva on 12/15/16.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME +
                "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + " TEXT, " +
                CrimeTable.Cols.TITLE + " TEXT, " +
                CrimeTable.Cols.DATE + " INTEGER, " +
                CrimeTable.Cols.SOLVED + " NUMERIC ," +
                CrimeTable.Cols.SUSPECT + " TEXT " +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
