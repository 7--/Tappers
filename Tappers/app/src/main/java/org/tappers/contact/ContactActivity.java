package org.tappers.contact;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tappers.MainActivity;
import org.tappers.transaction.NewTransaction;
import org.tappers.R;
import org.tappers.adapter.TransactionListAdapter;
import org.tappers.util.ActivityUtils;

import java.lang.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


public class ContactActivity extends AppCompatActivity {

    private Contact contact;
    private HashMap<String, Typeface> fonts;
    private ListView transactionList = null;
    public TextView txtTotal;
    private TransactionListAdapter transactionListAdapter;
    private int position;
    private String name;
    private String TAG = "ContactActivity";
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        txtTotal = (TextView) findViewById(R.id.txtTotal);

        fonts = new HashMap<>();
        transactionList = (ListView) findViewById(R.id.lstTransaction);

        Typeface thin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        fonts.put("thin", thin);
        fonts.put("light", light);
        fonts.put("regular", regular);


        txtTotal.setTypeface(fonts.get("light"));
        txtTotal.setText(getIntent().getStringExtra("total"));

        TextView txtHistory = (TextView) findViewById(R.id.txtHistory);

        txtHistory.setTypeface(fonts.get("light"));
        name = getIntent().getStringExtra("name").toString();
        ab.setTitle(name);
        ImageView characterImageContact = (ImageView) findViewById(R.id.characterImageContact);

        position = MainActivity.getPositionForContact(getIntent().getStringExtra("name").toString());

        contact = MainActivity.contacts.get(position);

        Character charType = Character.getCharacterForName(contact.characterType);
        CharacterBackground charBackground =
                CharacterBackground.getBackgroundForId(contact.backgroundColour);

        characterImageContact.setImageResource(charType.getCharacterFile());

        RelativeLayout charBack = (RelativeLayout) findViewById(R.id.character_background);

        charBack.setBackgroundResource(charBackground.getLargeBackground());

/*
        btnNewTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewTransaction.class);

                intent.putExtra("pos", position);
                startActivityForResult(intent, 1);
            }
        });
*/
        ImageButton btnClearHistory = (ImageButton) findViewById(R.id.btnClearHistory);

        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (contact.transactions.size() == 0) {
                                    Toast.makeText(getApplicationContext(), "There is no history to delete!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                contact.transactions.clear();
                                updateContactList();
                                contact.setTotalString();
                                txtTotal.setText(contact.total);
                                MainActivity.save.Save();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to clear the history for " + contact.name)
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });

        /*
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBackContact);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("name", position);
                setResult(ActivityUtils.CONTACT_RETURN, intent);
                finish();
            }
        });
        */
        updateContactList();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_page, menu);
        MenuItem menuItem = menu.findItem(R.id.share_invoice);
        MenuItem menuItem2 = menu.findItem(R.id.new_transaction);

        /*
        MenuItem menuShare = menu.findItem(R.id.share_invoice);
        shareActionProvider = new ShareActionProvider(this);
        MenuItemCompat.setActionProvider(menuShare, shareActionProvider);
        */

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_transaction:
                Intent intent = new Intent(this, NewTransaction.class);
                intent.putExtra("pos", position);
                startActivityForResult(intent, 1);
                return true;
            case R.id.share_invoice:
                String message = contact.toString();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invoice");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));
            default:
                return false;
        }
    }

    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "text");
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * Overriden method speaks with other intents
     * 0 = new Contact
     *
     * @param requestCode - the request coming in
     * @param resultCode  - the result coming in
     * @param data        - the intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == 1) {
                updateContactList();
                contact.setTotalString();
                txtTotal.setText(contact.total);
                data.putExtra("pos", position);
                MainActivity.save.Save();
            }
        }
    }

    public void updateContactList() {
        if(contact.transactions!=null) {
            for (int i = 0; contact.transactions != null && i < contact.transactions.size(); i++) {
                if (contact.transactions.get(i).getAmount() == 0) {
                    contact.transactions.remove(i);
                }
            }
        }
        transactionListAdapter = new TransactionListAdapter(getApplicationContext(), contact, fonts, this);

        transactionList.setAdapter(transactionListAdapter);
    }


}
//