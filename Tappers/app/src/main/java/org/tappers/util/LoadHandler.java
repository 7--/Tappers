package org.tappers.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.tappers.contact.Contact;
import org.tappers.transaction.Transaction;
import org.tappers.transaction.TransactionType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Zack on 16/12/2015.
 */
public class LoadHandler
{
    private SQLiteDatabase db;
    private ArrayList<Contact> contacts;

    public ArrayList<Contact> GetContacts()
    {
        Cursor mCursor = db.query("Contact",
                new String[]{"_id", "FirstName","LastName", "Date","CharacterType","BackgroundColor","PhoneNumber","Email", "ImageResourceId"},
                null, null, null, null, null);
        ArrayList<Contact> mArrayList = new ArrayList<Contact>();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            Contact c = new Contact(
                    mCursor.getString(mCursor.getColumnIndex("FirstName")).concat(" ".concat(mCursor.getString(mCursor.getColumnIndex("LastName")))),
                    "0.00",
                    mCursor.getString(mCursor.getColumnIndex("Date")),
                    mCursor.getString(mCursor.getColumnIndex("CharacterType")),
                    mCursor.getString(mCursor.getColumnIndex("BackgroundColor"))
                    );
            mArrayList.add(c);
        }
        return mArrayList;
        //TODO get transactions and give to contact
        //Contact c = new Contact();
    }

    public String loaderString = "";

    private Context context;
    public TabDatabaseHelper tabDatabaseHelper;
    public LoadHandler(Context context)
    {
        this.context = context;
        TabDatabaseHelper tabDatabaseHelper = new TabDatabaseHelper(context);
        db = tabDatabaseHelper.getReadableDatabase();

    }


   /* public void load()
    {
        contacts = new ArrayList<>();

        try {
            InputStream inputStream = context.openFileInput("tappers.txt");

            if(inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader((inputStream));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String temp;
                StringBuilder strBuilder = new StringBuilder();

                while( (temp = bufferedReader.readLine()) != null ) {
                    strBuilder.append(temp);
                }
                inputStream.close();
                loaderString = strBuilder.toString();
            }

        } catch(FileNotFoundException e) {
        } catch(IOException e) { }


        try
        {
            ArrayList<Transaction> tempTrans;
            Contact currentContact;

            String[] sectors = loaderString.split(";");
            Log.d("abc", "Loading String: " + loaderString);

            for(int i = 0; i < sectors.length; i++)
            {
                tempTrans = new ArrayList<>();
                String[] segments = sectors[i].split(":");
                String name = segments[0];
                String total = segments[1];
                String date = segments[2];

                String character = segments[3];

                String background = segments[4];

                String[] transactionSegments = segments[5].split("-");

                if((transactionSegments.length) == 0)
                {
                    currentContact = new Contact(name, total, date, character, background);
                    contacts.add(currentContact);
                }
                else
                {

                    for(int x = 0; x < transactionSegments.length; x++)
                    {
                        String[] newSeg = transactionSegments[x].split("%");

                        String reason = newSeg[0];
                        double amount = Double.parseDouble(newSeg[1]);
                        String dateTran = newSeg[2];

                        TransactionType type =
                                TransactionType.valueOf(newSeg[3].toUpperCase());

                        Transaction trans = new Transaction(type, amount, dateTran, reason);
                        Log.d("abc", "added transaction: " + type  );
                        tempTrans.add(trans);
                    }

                    currentContact = new Contact(name, total, date, character, background, tempTrans);

                    Log.d("abc", "ADDED NEW CONTACT");
                    contacts.add(currentContact);
                }
            }
        } catch(Exception io) {

            Log.d("abc", "IROOO " + io.toString());
        }
    }
*/
}
