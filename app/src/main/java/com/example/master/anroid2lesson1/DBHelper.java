package com.example.master.anroid2lesson1;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Master on 19.01.2017.
 */

public abstract class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static boolean execSQL(SQLiteDatabase db, String sql){
        if (db == null) return false;

		/* Try to execute SQL request */
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            return false;
        }

		/* Return true value */
        return true;
    }
}
