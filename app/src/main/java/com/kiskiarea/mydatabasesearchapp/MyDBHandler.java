package com.kiskiarea.mydatabasesearchapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Melissa on 11/11/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper
{
    static final String KEY_ATOMIC_NUMBER = "_atomic_number";
    static final String KEY_ATOMIC_WEIGHT = "atomic_weight";
    static final String KEY_NAME = "name";
    static final String KEY_SYMBOL = "symbol";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "PeriodicDB";
    static final String TABLE_PERIODIC = "periodic_table";
    static final int DATABASE_VERSION = 1;


    public MyDBHandler(Context context,String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_PERIODIC_TABLE = "CREATE TABLE " + TABLE_PERIODIC + "("
                + KEY_ATOMIC_NUMBER + " INTEGER PRIMARY KEY,"
                + KEY_ATOMIC_WEIGHT + " NUMERIC, "
                + KEY_NAME + " TEXT, "
                + KEY_SYMBOL + " TEXT" + ")";


        db.execSQL(CREATE_PERIODIC_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERIODIC);
        onCreate(db);

    }


    public void addElement(Elements ele)
    {
        ContentValues values = new ContentValues();

        values.put(KEY_ATOMIC_NUMBER, ele.get_atomic_number());
        values.put(KEY_ATOMIC_WEIGHT, ele.get_atomic_weight());
        values.put(KEY_NAME, ele.get_name());
        values.put(KEY_SYMBOL, ele.get_symbol());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PERIODIC, null, values);
        db.close();

    };

    public Elements findElement(String elementname)
    {
        String query = "Select * FROM " + TABLE_PERIODIC + " WHERE " + KEY_NAME + " = \"" + elementname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Elements elements = new Elements();

        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            elements.set_atomic_number(Integer.parseInt(cursor.getString(0)));
            elements.set_atomic_weight(Double.parseDouble(cursor.getString(1)));
            elements.set_name(cursor.getString(2));
            elements.set_symbol(cursor.getString(3));
            cursor.close();

        }
        else
        {
            elements = null;
        }
        db.close();

        return elements;

    }

    public boolean deleteElement(String elementname)
    {
        boolean result = false;

        String query = "Select * FROM " + TABLE_PERIODIC + " WHERE " + KEY_NAME + " = \"" + elementname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Elements elements = new Elements();

        if(cursor.moveToFirst())
        {
            elements.set_atomic_number(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PERIODIC, KEY_ATOMIC_NUMBER + " = ?",
                    new String[]{String.valueOf(elements.get_atomic_number())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;

    }

}
