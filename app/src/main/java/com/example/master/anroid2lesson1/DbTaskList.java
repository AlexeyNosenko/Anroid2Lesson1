package com.example.master.anroid2lesson1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import static com.example.master.anroid2lesson1.DbTaskList.TblTasks.CLN_REF_MAIN_TASK;
import static com.example.master.anroid2lesson1.DbTaskList.TblTasks.TBL_NAME;

/**
 * Created by Master on 19.01.2017.
 */

public class DbTaskList extends DBHelper {

    public static final String DB_NAME = "Task_list";
    public static final int DB_VERSION = 1;

    public static final int EMPTY_MAIN_TASK = -1;

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

    public Cursor getAllTasks() {
        return this.getWritableDatabase().query(TblTasks.TBL_NAME, null, null, null, null, null, null);
    }

    public long addTask(String task){
        return addTask(task, EMPTY_MAIN_TASK);
    }

    public long addTask(String task, long mainTaskId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TblTasks.CLN_TASK, task);
        if (mainTaskId != EMPTY_MAIN_TASK) {
            contentValues.put(TblTasks.CLN_REF_MAIN_TASK, mainTaskId);
        }
        return addTask(contentValues);
    }

    public long addTask(ContentValues contentValues){
        return this.getWritableDatabase().insert(TblTasks.TBL_NAME, null, contentValues);
    }

    public Cursor getTasks(){
        return getTasks(EMPTY_MAIN_TASK);
    }

    public Cursor getTasks(long mainTask){
        String where = mainTask == EMPTY_MAIN_TASK ? CLN_REF_MAIN_TASK + " is null " : CLN_REF_MAIN_TASK + " = " + mainTask;
//        Log.d("TaskList", "getTasks: " + where);
        return this.getWritableDatabase().query(TblTasks.TBL_NAME, null, where, null, null, null, null);
    }

    public long deleteSubTasks(long main_id){
        return this.getWritableDatabase().delete(TBL_NAME, TblTasks.CLN_REF_MAIN_TASK + " = " + main_id, null);
    }

    public long deleteTask(long id){
        Log.d("LG", "delete " + id);
        deleteSubTasks(id);
        return this.getWritableDatabase().delete(TblTasks.TBL_NAME, TblTasks._ID + " = " + id, null);
    }

    public DbTaskList(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TaskList", "onCreate: Create DB");
        DBHelper.execSQL(db, TblTasks.SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
