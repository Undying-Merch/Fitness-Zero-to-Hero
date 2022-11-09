package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Tilmeld extends AppCompatActivity {

    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilmeld);
    }

    public void createCust(View view){

        EditText nameText = (EditText) findViewById(R.id.editTextTextPersonName3);
        EditText mailText = (EditText) findViewById(R.id.editTextTextPersonName4);
        EditText phoneText = (EditText) findViewById(R.id.editTextPhone);


    }
}