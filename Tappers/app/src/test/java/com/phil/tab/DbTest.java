package com.phil.tab;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import android.content.Context;

import junit.framework.Assert;

import org.mockito.runners.MockitoJUnitRunner;
import org.tappers.util.*;
import org.junit.Test;

/**
 * Created by Phil on 12/22/2016.
 */
    @RunWith(MockitoJUnitRunner.class)
    public class DbTest {

    private Cursor cursor;

    @Mock
    Context mMockContext;
    @Mock
    SQLiteOpenHelper tabDatabaseHelper;
    @Mock
    private SQLiteDatabase db ;
    @Test
    public void ReadContactTable(){
        tabDatabaseHelper = new TabDatabaseHelper(mMockContext);
        db= tabDatabaseHelper.getReadableDatabase();
        cursor = db.query("DRINK",
                new String[]{"_id", "NAME"},
                null, null, null, null, null);
        Assert.assertNotNull(cursor);
        Assert.assertTrue(cursor.moveToFirst());
        String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
        String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
        Assert.assertNotNull(firstName);
        Assert.assertNotNull(lastName);
    }
}
