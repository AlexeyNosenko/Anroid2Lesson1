package com.example.master.anroid2lesson1;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private ListView lvList = null;

    private final int MENU_LIST_ADD = 1;
    private final int MENU_LIST_DELETE = 2;

    private final String DL_TITLE = "Enter task";
    private final String DL_MESSAGE = "";
    private final String DL_BTN_POSITIVE = "Ok";
    private final String DL_BTN_NEGATIVE = "Cancel";

    public final String LOG_TAG = "TaskList";

    private Cursor cursor;

    private DbTaskList dbTaskList;

    private Stack<Long> transitionTask = new Stack<>();
    private long currentTask = DbTaskList.EMPTY_MAIN_TASK;

    @Override
    public void onBackPressed() {
        if (transitionTask.empty()) {
            super.onBackPressed();
        } else {
            long task = transitionTask.pop();
            currentTask = task;
            Log.d(LOG_TAG, "onBackPressed " + task);
            getTasks(task);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvList = (ListView) findViewById(R.id.lvListTask);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "onItemListClick: " + currentTask);
                transitionTask.push(currentTask);
                currentTask = id;
                getTasks(id);
            }
        });

        registerForContextMenu(lvList);

        dbTaskList = new DbTaskList(this);
        getTasks(DbTaskList.EMPTY_MAIN_TASK);
    }

    private void getTasks(long task_id){
        cursor = dbTaskList.getTasks(task_id);
        startManagingCursor(cursor);
//        Log.d(LOG_TAG, "cursor.getCount(): " + cursor.getCount());
        updateList();
    }

    private void updateList(){
        cursor.requery();
        String[] header = new String[]{ DbTaskList.TblTasks.CLN_TASK };
        SimpleCursorAdapter userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, header, new int[] {android.R.id.text1}, 0);
        lvList.setAdapter(userAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.lvListTask:
                menu.add(0, MENU_LIST_ADD, 0, getResources().getString(R.string.menu_context_add));
                menu.add(0, MENU_LIST_DELETE, 0, getResources().getString(R.string.menu_context_delete));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_LIST_ADD:
                addRowInList(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).id);
                break;

            case MENU_LIST_DELETE:
                deleteRowInList(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteRowInList(long id) {
        dbTaskList.deleteTask(id);
        updateList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            addRowInList(currentTask);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addRowInList(final long id) {
        Log.d(LOG_TAG, "addRowInList: " + id);
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        dl.setTitle(DL_TITLE);
        dl.setMessage(DL_MESSAGE);

        final EditText newTask = new EditText(this);

        dl.setView(newTask);

        dl.setPositiveButton(DL_BTN_POSITIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, "onClickPositive: " + newTask.getText().toString());
                dbTaskList.addTask(newTask.getText().toString(), id);
                updateList();
            }
        });

        dl.setNegativeButton(DL_BTN_NEGATIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, "onClickNegative");
            }
        });
        dl.show();
    }

    @Override
    protected void onDestroy() {
        dbTaskList.close();
        super.onDestroy();
    }
}
