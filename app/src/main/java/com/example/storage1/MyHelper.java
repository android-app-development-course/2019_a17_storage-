package com.example.storage1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context){
        super(context,"itcast.db",null,2);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE label(id INTEGER PRIMARY KEY AUTOINCREMENT, pid VARCHAR(3),label VARCHAR(50)) ");
        db.execSQL("CREATE TABLE class(id INTEGER PRIMARY KEY AUTOINCREMENT, pid VARCHAR(3),name VARCHAR(50)) ");
        db.execSQL("CREATE TABLE loca(id INTEGER PRIMARY KEY AUTOINCREMENT, pid VARCHAR(3),name VARCHAR(50)) ");
        db.execSQL("CREATE TABLE goods(_id INTEGER PRIMARY KEY AUTOINCREMENT,id INTEGER default(-1),pid VARCHAR(3),class VARCHAR(50),name VARCHAR(50),label VARCHAR(100),value VARCHAR(100),img BLBO,location VARCHAR(50))");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}
}
