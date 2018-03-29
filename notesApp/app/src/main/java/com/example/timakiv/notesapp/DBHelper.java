package com.example.timakiv.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context,"NotesDB",null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Notes(note VARCHAR);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Notes");
        onCreate(db);
    }

    public void insertNote (String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note);
        db.insert("Notes", null, contentValues);
    }

    public void deleteNote (String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Notes",
                "note = ? ",
                new String[] { note });
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultSet = db.rawQuery("SELECT * FROM Notes",null);
        return resultSet;
    }

    public void deleteAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Notes", null, null);
    }
}