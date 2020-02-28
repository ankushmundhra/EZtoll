package com.example.eztoll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class NfcTransactionsActivity extends AppCompatActivity {

    DatabaseHelper mydb;
    private View mRootView;
    //SQLiteDatabase mDatabase;
    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "nfc_transaction_table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_transactions);
        mydb = new DatabaseHelper(this);

        //mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        TableLayout tableLayout=(TableLayout)findViewById(R.id.table);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#C5CAE9"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"ID","NAME","DATE","AMT_DEBITED"};   //changed here
        for(String c:headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        SharedPreferences prefs = getSharedPreferences("ank", MODE_PRIVATE);
        String name1 = prefs.getString("name", "No name defined");//"No name defined" is the default value.

        try
        {
            String selectQuery = "SELECT * FROM nfc_transaction_table WHERE NAME = "+"\""+name1+"\"" ;
            Cursor cursor = db.rawQuery(selectQuery,null);

            if (cursor.getCount() == 0) {
                //showMessage("Error", "Nothing Found");
                return;
            }
            //cursor.moveToFirst();

                while (cursor.moveToNext()) {
                    // Read columns data
                    int id = cursor.getInt(cursor.getColumnIndex("ID"));
                    String name = cursor.getString(cursor.getColumnIndex("NAME"));
                    String date = cursor.getString(cursor.getColumnIndex("DATE"));
                    String amt_debited = cursor.getString(cursor.getColumnIndex("AMT_DEBITED"));
                    String id1 = String.valueOf(id);

                    // data rows
                    TableRow row = new TableRow(this);
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));
                    String[] colText={id1,name,date,amt_debited};
                    for(String text:colText) {
                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(16);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);
                    }
                    tableLayout.addView(row);
                 }
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
            // Close database
        }

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    }

