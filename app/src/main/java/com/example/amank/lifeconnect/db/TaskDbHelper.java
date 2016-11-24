package com.example.amank.lifeconnect.db;

/**
 * Created by Yogesh on 11/13/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper{
    public TaskDbHelper(Context context) {
        super(context, com.example.amank.lifeconnect.db.TaskContract.DB_NAME, null, com.example.amank.lifeconnect.db.TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + com.example.amank.lifeconnect.db.TaskContract.TaskEntry.TABLE + " ( " +
                com.example.amank.lifeconnect.db.TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                com.example.amank.lifeconnect.db.TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + com.example.amank.lifeconnect.db.TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}