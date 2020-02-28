package com.example.eztoll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "registration_table";
    public static String DB_PATH = "/data/data/com.example.eztoll/databases/";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PASSWORD";
    public static final String COL_5 = "GENDER";
    public static final String COL_6 = "MOBILE_NUMBER";
    public static final String COL_7 = "DOB";

    public static final String TABLE_NAME2 = "vehicle_registration_table";
    public static final String VCOL_1 = "ID";
    public static final String VCOL_2 = "NAME";
    public static final String VCOL_3 = "MODEL";
    public static final String VCOL_4 = "TYPE";
    public static final String VCOL_5 = "ENGINE_NO";
    public static final String VCOL_6 = "VEHICLE_NO";
    public static final String VCOL_7 = "FUEL_TYPE";
    public static final String VCOL_8 = "DRIVING_LICENCE_NO";
    public static final String VCOL_9 = "LICENCE_STATUS";

    public static final String TABLE_NAME3 = "toll_location_table";
    public static final String LCOL_1 = "ID";
    public static final String LCOL_2 = "NAME";
    public static final String LCOL_3 = "DATE";
    public static final String LCOL_4 = "TIME";
    public static final String LCOL_5 = "LATITUDE";
    public static final String LCOL_6 = "LONGITUDE";

    public static final String TABLE_NAME4 = "transaction_detail_table";
    public static final String TCOL_1 = "ID";
    public static final String TCOL_2 = "DATE";
    public static final String TCOL_3 = "NAME";
    public static final String TCOL_4 = "ORDER_ID";
    public static final String TCOL_5 = "AMT_RECHARGED";

    public static final String TABLE_NAME5 = "nfc_transaction_table";
    public static final String NCOL_1 = "ID";
    public static final String NCOL_2 = "NAME";
    public static final String NCOL_3 = "DATE";
    public static final String NCOL_4 = "AMT_DEBITED";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAIL TEXT,PASSWORD TEXT,GENDER TEXT," +
                "MOBILE_NUMBER TEXT,DOB TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,MODEL TEXT,TYPE TEXT,ENGINE_NO TEXT,VEHICLE_NO TEXT," +
                "FUEL_TYPE TEXT,DRIVING_LICENCE_NO TEXT,LICENCE_STATUS TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME3 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE TEXT,TIME TEXT,LATITUDE TEXT,LONGITUDE TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME4 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,NAME TEXT,ORDER_ID TEXT,AMT_RECHARGED TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME5 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE TEXT,AMT_DEBITED TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,String email,String password,String gender,String mobile_number,String date_of_birth){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,email);
        contentValues.put(COL_4,password);
        contentValues.put(COL_5,gender);
        contentValues.put(COL_6,mobile_number);
        contentValues.put(COL_7,date_of_birth);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertVData(String name,String model,String type,String engine_no,String vehicle_no,String fuel_type,String driving_licence_no,String licence_status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VCOL_2,name);
        contentValues.put(VCOL_3,model);
        contentValues.put(VCOL_4,type);
        contentValues.put(VCOL_5,engine_no);
        contentValues.put(VCOL_6,vehicle_no);
        contentValues.put(VCOL_7,fuel_type);
        contentValues.put(VCOL_8,driving_licence_no);
        contentValues.put(VCOL_9,licence_status);
        long result = db.insert(TABLE_NAME2,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertLData(String name,String date,String time,String latitude,String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LCOL_2,name);
        contentValues.put(LCOL_3,date);
        contentValues.put(LCOL_4,time);
        contentValues.put(LCOL_5,latitude);
        contentValues.put(LCOL_6,longitude);
        long result = db.insert(TABLE_NAME3,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertTData(String date,String name,String order_id,String amt_recharged){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TCOL_2,date);
        contentValues.put(TCOL_3,name);
        contentValues.put(TCOL_4,order_id);
        contentValues.put(TCOL_5,amt_recharged);
        long result = db.insert(TABLE_NAME4,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertnfcData(String name,String date,String amt_debited){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NCOL_2,name);
        contentValues.put(NCOL_3,date);
        contentValues.put(NCOL_4,amt_debited);
        long result = db.insert(TABLE_NAME5,null,contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

//        public String[] getalldata() {
//            String Table_Name="registration_table";
//
//            String selectQuery = "SELECT  * FROM " + Table_Name;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            String[] data = null;
//            if (cursor.moveToFirst()) {
//                do {
//                    // get  the  data into array,or class variable
//                } while (cursor.moveToNext());
//            }
//            db.close();
//            return data;
//        }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getamt() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME4,null);
        return res;
    }

    public Cursor getlld() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME3,null);
        return res;
    }

}
