package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void gotoPersonalMain(View view){
        Intent intent = new Intent(Login.this, PersonalMain.class);
        startActivity(intent);
        this.finish();
    }
}