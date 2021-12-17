package com.example.kotov_majorov_stadnichenko_samost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends AppCompatActivity {

    TextView txtUsername, txtPassword;
    Button buttLogin, buttSignIn;

    //DBHelper dbHelper;
    //SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        buttLogin = findViewById(R.id.buttLogin);
        buttSignIn = findViewById(R.id.buttSignIn);

        buttSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtUsername.getText().toString()) || TextUtils.isEmpty(txtPassword.getText().toString()))
                {
                    String message = "All inputs required...";
                    Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
                }
                else {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setEmail("zaglushka");
                    registerRequest.setUsername(txtUsername.getText().toString());
                    registerRequest.setPassword(txtPassword.getText().toString());
                    RegisterUser(registerRequest);
                }
            }
        });

        buttLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtUsername.getText().toString()) || TextUtils.isEmpty(txtPassword.getText().toString()))
                {
                    String message = "All inputs required...";
                    Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
                }
                else{
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(txtUsername.getText().toString());
                    loginRequest.setPassword(txtPassword.getText().toString());

                    loginUser(loginRequest);
                }
            }
        });
    }

    public void RegisterUser(RegisterRequest registerRequest)
    {
        Call<RegisterResponse> registerResponseCall = ApiClient.getService().registerUsers(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
            if(response.isSuccessful())
            {
                String message = "Successful";
                Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
            }
            else
            {
                String message = "An error occurred";
                Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginUser(LoginRequest loginRequest)
    {
        Call<LoginResponse> loginResponseCall = ApiClient.getService().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful())
                {
                    LoginResponse loginResponse = response.body();
                    startActivity(new Intent(Auth.this, MainActivity.class).putExtra("data", loginResponse));
                    finish();
                }
                else
                {
                    String message = "An error occurred";
                    Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(Auth.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}