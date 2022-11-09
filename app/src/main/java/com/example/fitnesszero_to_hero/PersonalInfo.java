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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitnesszero_to_hero.Classes.Kunder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalInfo extends AppCompatActivity {

    int kdId;
    boolean editing = false;
    RequestQueue requestQueue;
    private String kunderURL = "http://10.0.2.2:8000/data/kundeliste/?format=json";
    private String updateKunde = "http://10.0.2.2:8000/data/kunde/";
    private ArrayList<Kunder> kunderne = new ArrayList<>();

    private void putIntoList(int id, String name, String phone, String mail){
        Kunder kunde = new Kunder(id, name, mail, phone);
        kunderne.add(kunde);
    }
    private void displayInfo(int id, String name, String phone, String mail){
        EditText textName = (EditText) findViewById(R.id.personalInfoName);
        EditText textEmail = (EditText)findViewById(R.id.personalInfoMail);
        EditText textPhone = (EditText)findViewById(R.id.personalInfoPhone);

        if (id == kdId){
            textName.setText(name);
            textEmail.setText(mail);
            textPhone.setText(phone);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        kdId = getIntent().getIntExtra("kundeId", 0);
        String name = getIntent().getStringExtra("name");
        String mail = getIntent().getStringExtra("mail");
        String phone = getIntent().getStringExtra("phone");

        EditText textName = (EditText) findViewById(R.id.personalInfoName);
        EditText textEmail = (EditText)findViewById(R.id.personalInfoMail);
        EditText textPhone = (EditText)findViewById(R.id.personalInfoPhone);



        textName.setEnabled(false);
        textEmail.setEnabled(false);
        textPhone.setEnabled(false);

        requestQueue = Volley.newRequestQueue(this);
        getUsers();
    }

    public void gotoPersonalAbbonement(View view){
        Intent intent = new Intent(PersonalInfo.this, PersonalAbonnement.class);
        startActivity(intent);
    }

    public void getUsers(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, kunderURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String kdName = jsonObject.getString("navn");
                        String mail = jsonObject.getString("mail");
                        String phone = jsonObject.getString("mobil");
                        boolean paid = jsonObject.getBoolean("betalt");
                        displayInfo(id, kdName, phone, mail);
                    }
                } catch (Exception w) {
                    Toast.makeText(PersonalInfo.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalInfo.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void updateUser(int id, String name, String phone, String mail) {
        StringRequest request = new StringRequest(Request.Method.PUT, updateKunde + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PersonalInfo.this, "Info updated", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalInfo.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("navn", name);
                params.put("mobil", phone);
                params.put("mail", mail);
                return params;
            }
        };
        requestQueue.add(request);
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
                editBTN.setText("Ændre Oplysninger:");
                editing = false;

                String name = textName.getText().toString();
                String mail = textEmail.getText().toString();
                String phone = textPhone.getText().toString();
                //TODO get update URL from Nico.
                updateUser(kdId, name, phone, mail);
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