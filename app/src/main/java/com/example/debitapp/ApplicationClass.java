package com.example.debitapp;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application {
    public static ArrayList<Photcopy> photcopyList;
    MyDataBaseHelper mydb;
    @Override
    public void onCreate() {
        super.onCreate();
        mydb=new MyDataBaseHelper(getApplicationContext());
        photcopyList=new ArrayList<>();
        Cursor res=mydb.viewRecords();


        Log.d("COunt", "onCreate: "+res.getCount());

        while(res.moveToNext())
        {
            String name=res.getString(1);
            Double ph=res.getDouble(2);
            Double pr=res.getDouble(3);
            Double tr=res.getDouble(4);
            Double price=res.getDouble(5);
            photcopyList.add(new Photcopy(name,ph,pr,tr,price));
        }

            mydb.close();
    }
}
