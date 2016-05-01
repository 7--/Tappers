package org.tappers.contact;

import android.content.res.Resources;

import org.tappers.R;
import org.tappers.transaction.Transaction;
import org.tappers.transaction.TransactionType;
import org.tappers.util.App;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zack on 13/12/2015.
 */
@SuppressWarnings("serial")
public class Contact implements Serializable
{

    public String name;

    public String total;

    public String date;

    public String characterType;

    public String backgroundColour;

    public List<Transaction> transactions;

    public Contact(String name, String total, String date, String characterType,
                   String backgroundColour, List<Transaction> transactions)
    {
        this.name = name;
        this.total = total;
        this.date = date;
        this.backgroundColour = backgroundColour;
        this.characterType = characterType;
        this.transactions = transactions;
    }

    public Contact(String name, String total, String date, String characterType,
                   String backgroundColour)
    {
        this.name = name;
        this.total = total;
        this.date = date;
        this.characterType = characterType;
        this.backgroundColour = backgroundColour;
        this.transactions = null;
    }

    /**
     * Adds a transaction to the transactions list
     * @param transaction - the transaction being added in
     */
    public void addTransaction(Transaction transaction)
    {
        if (transactions == null)
            transactions = new ArrayList<>();

        transactions.add(0, transaction);
    }


    /**
     * Gets all transactions, calculates them all and totals up
     * then sets the total string to what the total transaction value is
     */
    public void setTotalString()
    {
        double value = 0D;

        for(Transaction t : transactions)
        {
            if(t.getType() == TransactionType.FROM)
            {
                value -= t.getAmount();
            }
            else
            {
                value += t.getAmount();
            }

        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        if(value == 0)
        {
            total = "You and " + name + " don't owe each other anything!";
        }
        else if(value < 0)
        {
            total = "You owe " + name + " a total of " + formatter.format(Math.abs(value));
        }
        else if(value > 0)
        {
            total = name + " owes you a total of " + formatter.format(Math.abs(value));
        }

    }

    public String toString(){
        //res used to get Strings
        Resources res = App.getContext().getResources();
        double value = 0D;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(res.getString(R.string.invoice));
        sb.append("\n");
        for(Transaction t : transactions) {
            sb.append(t.getDate());
            sb.append("\n");
            if(t.getType() == TransactionType.FROM) {
                sb.append(res.getString(R.string.you_lent_me));
                sb.append(formatter.format(Math.abs(t.getAmount())));
                if(!"".equals(t.getReason()))
                    sb.append(res.getString(R.string.forr));
                sb.append(t.getReason());
                value -= t.getAmount();
            }
            else {
                sb.append(res.getString(R.string.i_lent_you));
                sb.append(formatter.format(Math.abs(t.getAmount())));
                if(!"".equals(t.getReason()))
                    sb.append(res.getString(R.string.forr));
                sb.append(t.getReason());
                value += t.getAmount();
            }
            sb.append("\n");
        }
        sb.append("\n");
        if(value == 0) {
            sb.append(res.getString(R.string.we_dont_owe));
        }
        else if(value < 0) {
            sb.append(res.getString(R.string.i_owe_you) + formatter.format(Math.abs(value)));
        }
        else if(value > 0) {
            sb.append(res.getString(R.string.you_owe_me)  + formatter.format(Math.abs(value)));
        }
        return sb.toString();
    }

}
