package com.example.kotov_majorov_stadnichenko_samost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear, btnShop;
    EditText etName, etPrice;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnShop = (Button) findViewById(R.id.btnShop);
        btnShop.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }
    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);

                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputPrice);

                Button buttonDelete = new Button(this);
                buttonDelete.setOnClickListener(this);
                params.weight = 1.0f;
                buttonDelete.setLayoutParams(params);
                buttonDelete.setText("Удалить товар");
                buttonDelete.setTag("delete");
                buttonDelete.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(buttonDelete);

                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                contentValues = new ContentValues();
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_PRICE, price);
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                UpdateTable();
                etName.setText("");
                etPrice.setText("");
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            case R.id.btnShop:
                Intent intent = new Intent(this, Shop.class);
                startActivity(intent);
                break;
            default:
                if(v.getTag() == "delete") {
                    View outputDBRow = (View) v.getParent();
                    ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                    outputDB.removeView(outputDBRow);
                    outputDB.invalidate();
                    database.delete(dbHelper.TABLE_CONTACTS, dbHelper.KEY_ID + " = ?", new String[]{String.valueOf(v.getId())});

                    contentValues = new ContentValues();
                    Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                    if (cursorUpdater.moveToFirst()) {
                        int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                        int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                        int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                        int realID = 1;
                        do {
                            if (cursorUpdater.getInt(idIndex) > realID) {
                                contentValues.put(dbHelper.KEY_ID, realID);
                                contentValues.put(dbHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                                contentValues.put(dbHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                                database.replace(dbHelper.TABLE_CONTACTS, null, contentValues);
                            }
                            realID++;
                        }
                        while (cursorUpdater.moveToNext());
                        if (cursorUpdater.moveToLast()) {
                            if (cursorUpdater.moveToLast() && v.getId() != realID) {
                                database.delete(dbHelper.TABLE_CONTACTS, dbHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                            }
                        }
                        UpdateTable();
                    }
                }

            break;
        }
    }
}