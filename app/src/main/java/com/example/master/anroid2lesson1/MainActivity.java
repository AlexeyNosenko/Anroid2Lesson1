package com.example.master.anroid2lesson1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private ListView lvList = null;

    private final int MENU_LIST_ADD = 1;
    private final int MENU_LIST_DELETE = 2;

    private final String DL_TITLE = "Enter task";
    private final String DL_MESSAGE = "Enter task";
    private final String DL_BTN_POSITIVE = "Ok";
    private final String DL_BTN_NEGATIVE = "Cancel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvList = (ListView) findViewById(R.id.lvList);

        registerForContextMenu(lvList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.lvList:
                menu.add(0, MENU_LIST_ADD, 0, getResources().getString(R.string.menu_add));
                menu.add(0, MENU_LIST_DELETE, 0, getResources().getString(R.string.menu_delete));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_LIST_ADD:
                addData();
                break;

            case MENU_LIST_DELETE:
                // deleteRow(getDataFromRow(getSelectedrowNum))
                break;
        }
        return super.onContextItemSelected(item);
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
            addData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addData() {
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        dl.setTitle(DL_TITLE);
        dl.setMessage(DL_MESSAGE);

        EditText newTask = new EditText(this);

        dl.setView(newTask);

        dl.setPositiveButton(DL_BTN_POSITIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // addRowInDB
            }
        });

        dl.setNegativeButton(DL_BTN_NEGATIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
