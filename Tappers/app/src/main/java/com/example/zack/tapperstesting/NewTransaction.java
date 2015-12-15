package com.example.zack.tapperstesting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;


public class NewTransaction extends Activity {


    private int yearDate, monthDate, dayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);


        Calendar cal = Calendar.getInstance();

        yearDate = cal.get(Calendar.YEAR);
        monthDate = cal.get(Calendar.MONTH);
        dayDate = cal.get(Calendar.DAY_OF_MONTH);

        Button btnAccept = (Button) findViewById(R.id.cmdConfirmTrans);
        Button btnDate = (Button) findViewById(R.id.btnPickNewDateTran);

        final TextView amount = (TextView) findViewById(R.id.txtTransactionTran);
        final TextView reason = (TextView) findViewById(R.id.txtReasonTran);
        final TextView date = (TextView) findViewById(R.id.lblSetDateTran);

        final RadioButton to = (RadioButton) findViewById(R.id.rdbToTran);
        final RadioButton from = (RadioButton) findViewById(R.id.rdbFromTran);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double am = 1;
                try
                {
                    am = Double.parseDouble(amount.getText().toString());
                }
                catch(Exception e)
                {

                }
                String tofrom = "";

                if(to.isChecked())
                {
                    tofrom = "TO";
                }
                else
                {
                    tofrom = "FROM";
                }

                TransactionType type = TransactionType.valueOf(tofrom);

                ContactUtil.contact.addTransaction(new Transaction(
                        type, am, date.getText().toString().substring(15),
                        reason.getText().toString()));
                Intent returnIntent = getIntent();
                setResult(1, returnIntent);
                finish();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == 0)
        {
            return new DatePickerDialog(this, dPickerListener, yearDate, monthDate, dayDate);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    yearDate = year;
                    monthDate = monthOfYear + 1;
                    dayDate = dayOfMonth + 1;
                    TextView text = (TextView) findViewById(R.id.lblSetDateTran);
                    System.out.println();
                    text.setText("Date Selected: " + dayDate + "/" + monthDate + "/" + yearDate);
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
