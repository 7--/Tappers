package org.tappers.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.tappers.R;

public class TabDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Tab"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

    public TabDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE Contact (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "FirstName TEXT Not Null,"
                    + "LastName TEXT, "
                    + "Date TEXT, "
                    + "CharacterType Text, "
                    + "BackgroundColor TEXT, "
                    + "PhoneNumber TEXT, "
                    + "Email TEXT, "
                    + "ImageResourceId INTEGER);");
            db.execSQL("CREATE TABLE [Transaction] (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "Contact TEXT Not Null,"
                    + "Type TEXT Not Null,"
                    + "Amount TEXT, "
                    + "Date TEXT, "
                    + "Reason TEXT);");
            //Just get it to read an inserted one and then save contacts. Date format. Foreignkey. Unique
            insertContact(db, "Sponge", "Bob", "10/12/00",  "default male" ,"5555555555" ,"blue","your@email.com",R.drawable.gamer_bob_small );
        }
        //insertContact(db, "Filter", "Our best drip coffee", null, null, R.drawable.gamer_bob_small);
        if (oldVersion < 2) {
            //Version 1 so far. db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }
    }

    private static void insertContact(SQLiteDatabase db, String firstName, String lastName,
                                      String date, String characterType,
                                      String phoneNumber, String backgroundColor, String email,int imageResourceId){
        ContentValues contactValues = new ContentValues();
        contactValues.put("FirstName", firstName);
        contactValues.put("LastName", lastName);
        contactValues.put("Date",date);
        contactValues.put("CharacterType", characterType);
        contactValues.put("BackgroundColor", backgroundColor);
        contactValues.put("PhoneNumber", phoneNumber);
        contactValues.put("Email", email);
        contactValues.put("ImageResourceId", imageResourceId);
        db.insert("Contact",null,contactValues);
    }

}
