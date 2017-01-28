package com.example.hectorleyvavillanueva.criminalintent.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.hectorleyvavillanueva.criminalintent.model.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hectorleyvavillanueva on 12/15/16.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){

        String strUuid = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime.Builder()
                .id(UUID.fromString(strUuid))
                .title(title)
                .date(new Date(date))
                .solved(isSolved != 0)
                .suspect(suspect)
                .build();

        return crime;
    }
}
