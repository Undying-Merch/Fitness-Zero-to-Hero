package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartSite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_site);
    }

    public void gotoKontakt(View view){
        Intent intent = new Intent(StartSite.this, Kontakt.class);
        startActivity(intent);
    }
}