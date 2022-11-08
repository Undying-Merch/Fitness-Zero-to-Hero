package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


import com.example.fitnesszero_to_hero.Classes.Bruger;

public class Login extends AppCompatActivity {

    private String loginURL = "http://10.0.2.2:8000/data/brugerliste/?format=json";
    private ArrayList<Bruger> brugere = new ArrayList<>();
    RequestQueue requestQueue;

    public void putIntoBrugerList(String user, String pass, int id){
        Bruger bruger = new Bruger(user, pass, id);
        brugere.add(bruger);
    }

    private void getBrugere(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, loginURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String user = jsonObject.getString("brugernavn");
                        String pass = jsonObject.getString("password");
                        int brugerId = jsonObject.getInt("kundeid");
                        putIntoBrugerList(user, pass, brugerId);
                    }
                } catch (Exception w) {
                    Toast.makeText(Login.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        getBrugere();
    }

    public void gotoPersonalMain(View view){

        EditText userIn = findViewById(R.id.editTextTextPersonName2);
        EditText passwordIn = findViewById(R.id.editTextTextPassword);
        String user = userIn.getText().toString();
        String pass = passwordIn.getText().toString();
        boolean correct = false;

        Toast toast = Toast.makeText(this, "User og password incorrect", Toast.LENGTH_LONG);
        for (int i = 0; i < brugere.size(); i++){
            if (brugere.get(i).brugernavn.matches(user) && brugere.get(i).Password.matches(pass)){
                Intent intent = new Intent(Login.this, PersonalMain.class);
                intent.putExtra("KundeId", brugere.get(i).brugerId);
                startActivity(intent);
                this.finish();
                correct = true;
            }
        }
        if (correct == false){
            toast.show();
        }



    }
}