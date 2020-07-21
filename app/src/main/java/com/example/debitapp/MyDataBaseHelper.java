package com.example.debitapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;

import java.io.File;

public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String Database_name="Photocopy.db";
    private static final String table_name="photocopy_table";
    public static final String COL_1="ID";
    public static final String COL_2="Name";
    public static final String COL_3="Photocopy_Details";
    public static final String COL_4="Print_Details";
    public static final String COL_5="Transfer_Details";
    public static final String COL_6="Price";
    public MyDataBaseHelper(@Nullable Context context) {
        super(context,Database_name,null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE ="CREATE TABLE " + table_name + "("
                + COL_1+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 +
                " TEXT, " + COL_3 + " REAL, " + COL_4 + " REAL, "+ COL_5 +" REAL,"
                + COL_6 + " REAL " + ")";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           //  db.deleteDatabase(new File(PATH,Database_name));
            db.execSQL("DROP TABLE IF EXISTS " + table_name);
            onCreate(db);
    }

    public boolean insert_table(String name,double ph,double pr,double tr,double price)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,ph);
        contentValues.put(COL_4,pr);
        contentValues.put(COL_5,tr);
        contentValues.put(COL_6,price);
        long ins=db.insert(table_name,null,contentValues);
        db.close();
        if(ins==-1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor viewRecords()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+table_name,null);
        return res;
    }

    public boolean upgrade(String name,double ph,double pr,double tr,double price)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,ph);
        contentValues.put(COL_4,pr);
        contentValues.put(COL_5,tr);
        contentValues.put(COL_6,price);
        db.update(table_name,contentValues,"Name=?",new String[] {name});
        return true;
    }

    public int delete(String name)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete(table_name,"Name=?",new String[] {name});
        return res;

    }

}
