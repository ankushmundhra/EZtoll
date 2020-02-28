package com.example.eztoll;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class WalletManagementActivity extends AppCompatActivity {

    DatabaseHelper mydb;
    Button print_button;
    static String tbal;
    static int[] bal;
    public static final String DATABASE_NAME = "Registration.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_management);
        mydb = new DatabaseHelper(this);

        print_button = findViewById(R.id.printbtn);

        print_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.printbtn:
                        try {
                            createPdf();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                        break;
                    // ...
                }
            }
        });

        TableLayout tableLayout=(TableLayout)findViewById(R.id.table);
        // Add header row
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#C5CAE9"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"DATE","CREDIT","DEBIT","BALANCE"};   //changed here
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
        String name1 = prefs.getString("name", "No name defined");

        try
        {
//            "CREATE OR REPLACE view myview AS SELECT transaction_detail_table.DATE,AMT_RECHARGED,AMT_DEBITED " +
//                    "FROM transaction_detail_table join nfc_transaction_table on transaction_detail_table.NAME = nfc_transaction_table.NAME " +
//                    "and nfc_transaction_table.NAME ="+"\""+name1+"\""


            String selectquery = "CREATE view myview1 AS SELECT transaction_detail_table.DATE,AMT_RECHARGED,AMT_DEBITED " +
                    "FROM transaction_detail_table join nfc_transaction_table on transaction_detail_table.NAME = nfc_transaction_table.NAME " +
                    "and nfc_transaction_table.NAME ="+"\""+name1+"\"";

            String selectQuery = "SELECT * FROM myview1";

            Cursor cursor = db.rawQuery(selectQuery,null);
            if (cursor.getCount() == 0) {
                //showMessage("Error", "Nothing Found");
                return;
            }
            //cursor.moveToFirst();

            bal = new int[cursor.getCount()];
            int amtd = 0;
            int amtr = 0;
            int temp_bal = 0;
            int i = 0;
            while (cursor.moveToNext()) {
                // Read columns data
                String date = cursor.getString(cursor.getColumnIndex("DATE"));
                String amt_recharged = cursor.getString(cursor.getColumnIndex("AMT_RECHARGED"));
                String amt_debited = cursor.getString(cursor.getColumnIndex("AMT_DEBITED"));

                amtr = Integer.parseInt(amt_recharged);
                amtd = Integer.parseInt(amt_debited);
                bal[i] = (temp_bal + amtr) - amtd;
                temp_bal = bal[i];
                tbal = String.valueOf(bal[i]);

                // data rows
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String[] colText={date,amt_recharged,amt_debited,tbal};
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
                i++;
            }

            TextView textView7 = (TextView) findViewById(R.id.textView7);
            textView7.setText("₹" + bal[i-1] + ".00");
            SharedPreferences.Editor editor = getSharedPreferences("main_bal", MODE_PRIVATE).edit();
            editor.putInt("name7", bal[i-1]);
            editor.apply();
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


    }

    public void createPdf() throws FileNotFoundException, DocumentException {
        try {
            int i = 0;
            SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            String dir = Environment.getExternalStorageDirectory() + File.separator + "Download";
            File folder = new File(dir);
            folder.mkdirs();

            File file = new File(dir, "Transaction_History.pdf");

            Cursor c1 = db.rawQuery("SELECT * FROM myview1", null);
            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));

//            String userPassword = "123";
//            String ownerPassword = "admin";
//            PdfWriter.setEncryption(userPassword.getBytes(),
//                    ownerPassword.getBytes(),
//                    PdfWriter.ALLOW_PRINTING,
//                    PdfWriter.ENCRYPTION_AES_256);

            document.open();

            String head = "Transaction Details";
            Paragraph p3 = new Paragraph();
            //Paragraph p4 = new Paragraph();
            p3.setAlignment(Paragraph.ALIGN_CENTER);
            p3.add("Transaction History \n\n");
            document.add(p3);


            PdfPTable table = new PdfPTable(4);
            table.addCell("Date");
            table.addCell("Credit");
            table.addCell("Debit");
            table.addCell("Balance");

            while (c1.moveToNext()) {
                String date = c1.getString(0);
                String credit = c1.getString(1);
                String debit = c1.getString(2);
                //String balance = c1.getString(3);

                table.addCell(date);
                table.addCell(credit);
                table.addCell(debit);
                table.addCell(String.valueOf(bal[i++]));
            }

            document.add(table);
            document.addCreationDate();
            document.close();

            Toast.makeText(getApplicationContext(), "Document saved in sdcard/Download", Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

