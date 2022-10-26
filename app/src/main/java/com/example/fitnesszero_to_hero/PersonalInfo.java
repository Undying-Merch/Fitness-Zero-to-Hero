package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PersonalInfo extends AppCompatActivity {

    boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        EditText textName = (EditText) findViewById(R.id.personalInfoName);
        EditText textEmail = (EditText)findViewById(R.id.personalInfoMail);
        EditText textPhone = (EditText)findViewById(R.id.personalInfoPhone);

        textName.setEnabled(false);
        textEmail.setEnabled(false);
        textPhone.setEnabled(false);
    }

    public void gotoPersonalAbbonement(View view){
        Intent intent = new Intent(PersonalInfo.this, PersonalAbonnement.class);
        startActivity(intent);
    }

    //For changing personal info
    public void editInfo(View view){
        EditText textName = (EditText) findViewById(R.id.personalInfoName);
        EditText textEmail = (EditText)findViewById(R.id.personalInfoMail);
        EditText textPhone = (EditText)findViewById(R.id.personalInfoPhone);

        Button editBTN = (Button)findViewById(R.id.personalInfoEditBTN);
        Button abbBTN = (Button)findViewById(R.id.personalInfoAbnBTN);

        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfo.this);
        builder.setMessage("Are you sure you want to save?");

        //Dialog Listener for Builder
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textName.setEnabled(false);
                textEmail.setEnabled(false);
                textPhone.setEnabled(false);
                abbBTN.setVisibility(View.VISIBLE);
                editBTN.setText("Ændre Oplysninger");
                editing = false;
            }
        };

        if (editing == false){

            textName.setEnabled(true);
            textEmail.setEnabled(true);
            textPhone.setEnabled(true);
            abbBTN.setVisibility(View.GONE);
            editBTN.setText("Save");
            editing = true;
        }
        else if (editing){

            builder.setPositiveButton("Save", dialogListener);
            builder.show();

        }
    }
}