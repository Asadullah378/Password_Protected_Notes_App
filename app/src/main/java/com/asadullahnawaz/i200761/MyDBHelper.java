package com.asadullahnawaz.i200761;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    String CREATE_TABLE_NOTE="CREATE TABLE "+
            MyContracts.Note.TABLE_NAME+ "( "+
            MyContracts.Note._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            MyContracts.Note._TITLE + " TEXT NOT NULL, "+
            MyContracts.Note._DESCRIPTION + " TEXT NOT NULL, "+
            MyContracts.Note._IMAGE  + " TEXT NOT NULL, "+
            MyContracts.Note._LOCK_STATUS  + " TEXT NOT NULL, "+
            MyContracts.Note._PASSWORD  + " TEXT NOT NULL, "+
            MyContracts.Note._CATEGORY_ID  + " INTEGER NOT NULL, "+
            MyContracts.Note._DATE_CREATED + " TEXT NOT NULL,"+
            MyContracts.Note._DATE_UPDATED + " TEXT NOT NULL,"+
            MyContracts.Note._COLOR + " TEXT NOT NULL,"+
            "FOREIGN KEY ("+MyContracts.Note._CATEGORY_ID+") REFERENCES FLIGHT ("+MyContracts.Category._ID+"));";

    String DROP_TABLE_NOTE="DROP TABLE IF EXISTS "+ MyContracts.Note.TABLE_NAME;

    String CREATE_TABLE_CATEGORY="CREATE TABLE "+
            MyContracts.Category.TABLE_NAME+ "( "+
            MyContracts.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            MyContracts.Category._TITLE + " TEXT NOT NULL);";

    String DROP_TABLE_CATEGORY="DROP TABLE IF EXISTS "+ MyContracts.Category.TABLE_NAME;

    public MyDBHelper(@Nullable Context context) {
        super(context, MyContracts.DATABASE_NAME, null, MyContracts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL("INSERT INTO "+MyContracts.Category.TABLE_NAME+" VALUES (1,'Notes');");
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_NOTE);
        sqLiteDatabase.execSQL(DROP_TABLE_CATEGORY);
        onCreate(sqLiteDatabase);

    }
}