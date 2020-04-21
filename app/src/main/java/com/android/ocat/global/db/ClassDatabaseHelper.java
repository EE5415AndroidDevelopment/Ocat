package com.android.ocat.global.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassDatabaseHelper extends SQLiteOpenHelper {

    public ClassDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table courses(" +
                "id integer primary key autoincrement," +
                "course_name text," +
                "teacher text," +
                "class_room text," +
                "day integer," +
                "class_start integer," +
                "class_end integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor selectByDayOfWeek(int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from courses where day = ?", new String[]{Integer.toString(value)});
        return res;
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists courses");
        db.execSQL("create table courses(" +
                "id integer primary key autoincrement," +
                "course_name text," +
                "teacher text," +
                "class_room text," +
                "day integer," +
                "class_start integer," +
                "class_end integer)");
    }
}
