package com.android.ocat.global.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReminderDataBaseHelper extends SQLiteOpenHelper {
    public ReminderDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table reminder(" +
                "id integer primary key autoincrement," +
                "date text," +
                "detail text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(String date, String detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("detail", detail);
        long flag = db.insert("reminder", null, contentValues);
        if (flag == -1)
            return false;
        else
            return true;
    }

    public Cursor selectById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from reminder where id = ?", new String[]{id});
        return res;
    }

    public Cursor selectByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from reminder where date = ?", new String[]{date});
        return res;
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from reminder", null);
        return res;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long flag = db.delete("reminder", "id=?", new String[]{Integer.toString(id)});
        if (flag == -1)
            return false;
        else
            return true;
    }

    public boolean update(int id, String date, String detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",date);
        values.put("detail",detail);
        long flag = db.update("reminder", values, "id = ?", new String[]{Integer.toString(id)});
        if (flag == -1)
            return false;
        else
            return true;

    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists reminder");
        db.execSQL("create table reminder(" +
                "id integer primary key autoincrement," +
                "date text," +
                "detail text)");
    }
}
