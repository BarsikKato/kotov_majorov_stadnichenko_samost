package com.example.kotov_majorov_stadnichenko_samost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Auth extends AppCompatActivity implements View.OnClickListener {

    TextView txtUsername, txtPassword;
    Button buttLogin, buttSignIn;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        buttLogin = findViewById(R.id.buttLogin);
        buttLogin.setOnClickListener(this);
        buttSignIn = findViewById(R.id.buttSignIn);
        buttSignIn.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        //Добавление админа в базу для перехода к управлению магазином
//        ContentValues values = new ContentValues();
//        values.put(dbHelper.KEY_USERNAME, "admin");
//        values.put(dbHelper.KEY_PASSWORD, "admin");
//        database.insert(dbHelper.TABLE_USERS, null, values);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.buttLogin:
                Cursor logCursor = database.query(dbHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean logged = false;
                if(logCursor.moveToFirst())
                {
                    int IndexUsername = logCursor.getColumnIndex(dbHelper.KEY_USERNAME);
                    int IndexPassword = logCursor.getColumnIndex(dbHelper.KEY_PASSWORD);
                    do{
                        if(txtUsername.getText().toString().equals("admin") && txtPassword.getText().toString().equals("admin"))
                        {
                            startActivity(new Intent(this, MainActivity.class));
                            logged = true;
                            break;
                        }
                        else if(txtUsername.getText().toString().equals(logCursor.getString(IndexUsername)) && txtPassword.getText().toString().equals(logCursor.getString(IndexPassword)))
                        {
                            startActivity(new Intent(this, Shop.class));
                            logged = true;
                            break;
                        }
                    }while(logCursor.moveToNext());
                }
                logCursor.close();
                if (!logged) Toast.makeText(this, "Введенные логин и пароль не были найдены", Toast.LENGTH_LONG).show();
                break;

            case R.id.buttSignIn:
                Cursor signCursor = database.query(dbHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean finded=false;
                if(signCursor.moveToFirst())
                {
                    int IndexUsername = signCursor.getColumnIndex(dbHelper.KEY_USERNAME);
                    do{
                        if(txtUsername.getText().toString().equals(signCursor.getString(IndexUsername)))
                        {
                            Toast.makeText(this, "Такой логин уже существует", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    }while(signCursor.moveToNext());
                }
                if(!finded)
                {
                    ContentValues values = new ContentValues();
                    values.put(dbHelper.KEY_USERNAME, txtUsername.getText().toString());
                    values.put(dbHelper.KEY_PASSWORD, txtPassword.getText().toString());

                    database.insert(dbHelper.TABLE_USERS, null, values);
                    Toast.makeText(this, "Вы успешно зарегистрировались!", Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }
    }
}