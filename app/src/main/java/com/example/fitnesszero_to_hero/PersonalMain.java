package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PersonalMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_main);


    }

    public void gotoPersonalInfo(View view){
        Intent intent = new Intent(PersonalMain.this, PersonalInfo.class);
        startActivity(intent);
    }
}