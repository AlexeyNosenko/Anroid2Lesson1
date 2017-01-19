package com.example.master.anroid2lesson1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SyncAdapterType;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Master on 19.01.2017.
 */

public class DbTaskList extends DBHelper {

    public static final String DB_NAME = "Task_list";
    public static final int DB_VERSION = 1;

    public static class TblTasks implements BaseColumns {

        public static String TBL_NAME = "Tasks";

        public static String CLN_TASK = "task";
        public static String CLN_COMPLETE = "complete";
        public static String CLN_REF_MAIN_TASK = "refTask";

        public static final String SQL_CREATE_DB =
                "create table " + TBL_NAME + " ("
                        + _ID + " integer primary key autoincrement,"
                        + CLN_TASK + " text,"
                        + CLN_COMPLETE + " text,"
                        + CLN_REF_MAIN_TASK + " integer,"
                        + "FOREIGN KEY(" + CLN_REF_MAIN_TASK + ") REFERENCES Tasks(" + _ID + ")"
                        + ");";
    }

    public Cursor getAllData() {
        return this.getWritableDatabase().query(TblTasks.TBL_NAME, null, null, null, null, null, null);
    }

    public long addTask(String task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TblTasks.CLN_TASK, task);
        return this.getWritableDatabase().insert(TblTasks.TBL_NAME, null, contentValues);
    }

    public long deleteTask(long id){
        return this.getWritableDatabase().delete(TblTasks.TBL_NAME, TblTasks._ID + " = " + id, null);
    }

    public DbTaskList(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBHelper.execSQL(db, TblTasks.SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
