package com.example.hectorleyvavillanueva.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.hectorleyvavillanueva.criminalintent.model.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by hectorleyvavillanueva on 12/2/16.
 */

public class CrimeLab {

    private static final String TAG = CrimeLab.class.getSimpleName();
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    //Singleton
    public static CrimeLab get(Context context){
        Log.d(TAG,"static CrimeLab get(Context context)");
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //Constructor
    private CrimeLab(Context context){
        Log.d(TAG,"CrimeLab(Context context)");
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public List<Crime> getCrimeList(){
        Log.d(TAG,"List<Crime> getCrimeList()");
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);
        try{
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }finally {
            cursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id){
        Log.d(TAG,"Crime getCrime(UUID id)");
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        }catch(SQLException e){
            throw new SQLException(e.getMessage());
        }finally {
            cursor.close();
        }

    }

    public File getPhotoFile(Crime crime){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFileDir ==  null)
            return null;
        return new File(externalFileDir, crime.getPhotoFileName());
    }

    public void addCrime(final Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME,null,values);
    }

    public void updateCrime(Crime crime){
        String uuid = crime.getmId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME,values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuid});
    }

    public void deleteCrime(Crime crime){
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{crime.getmId().toString()});
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getmId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getmTitle());
        values.put(CrimeTable.Cols.DATE,crime.getmDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.ismSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getmSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

}
