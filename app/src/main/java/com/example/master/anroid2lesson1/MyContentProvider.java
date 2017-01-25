package com.example.master.anroid2lesson1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Nosenko on 24.01.2017.
 */

public class MyContentProvider extends ContentProvider {


    private static final String CONTENT = "content://";
    private static final String AUTHORUTY = "my.android.task.list";

    private static final int GTASK = 1;
    private static final String PATH_TASK = "tasks";

    public static final Uri CONTENT_URI_TASK = Uri.parse(CONTENT + AUTHORUTY + "/" + PATH_TASK);

    private static final UriMatcher uriMatcher;
    private static final String LOG_TAG = "LT";

    static {
        uriMatcher =  new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORUTY, PATH_TASK, GTASK);
    }

    private DbTaskList dbTaskList;

    @Override
    public boolean onCreate() {
        dbTaskList = new DbTaskList(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d("DB", "onItemListClick: selection = " + selection);
        Cursor cursor = dbTaskList.getReadableDatabase().query(DbTaskList.TblTasks.TBL_NAME, null, selection, null, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        if (uriMatcher.match(uri) == GTASK){
            id = dbTaskList.addTask(values);
        } else throw new IllegalArgumentException("Unknow URI " + uri);

        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(CONTENT_URI_TASK, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete " + selection);
        if (uriMatcher.match(uri) == GTASK){
            dbTaskList.deleteTask(Long.valueOf(selection));
        }else throw new IllegalArgumentException("Unknow URI " + uri);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
