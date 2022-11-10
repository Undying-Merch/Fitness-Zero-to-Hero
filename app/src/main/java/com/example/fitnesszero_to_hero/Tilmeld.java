package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.example.fitnesszero_to_hero.Classes.Bruger;
import com.example.fitnesszero_to_hero.Classes.Kunder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Tilmeld extends AppCompatActivity {

    private Kunder kunde = new Kunder();
    private Bruger bruger = new Bruger();
    private boolean phase1 = false;
    RequestQueue requestQueue;
    private String createBrugerURL = "http://10.0.2.2:8000/data/opretbruger/";
    private String createKundeURL = "http://10.0.2.2:8000/data/opretkunde/";
    private String getKunderURL = "http://10.0.2.2:8000/data/kundeliste/?format=json";



    private void getID(int id, String name){
        if (name.matches(kunde.name)){
            bruger.brugerId = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilmeld);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void createCust(View view){

        //TODO maybe make a popup to make sure?
        EditText nameText = (EditText) findViewById(R.id.editTextTextPersonName3);
        EditText mailText = (EditText) findViewById(R.id.editTextTextPersonName4);
        EditText phoneText = (EditText) findViewById(R.id.editTextPhone);
        Button button = (Button) findViewById(R.id.button11);

        if (phase1 == false){
            kunde.name = nameText.getText().toString();
            kunde.mail = mailText.getText().toString();
            kunde.phone = phoneText.getText().toString();

            nameText.getText().clear();
            mailText.getText().clear();
            phoneText.getText().clear();
            nameText.setHint("Brugernavn:");
            mailText.setHint("Password:");
            phoneText.setVisibility(View.INVISIBLE);
            phase1 = true;
            button.setText("Opret bruger");
            createKunde(kunde);
        }
        else if (phase1 == true){
            bruger.brugernavn = nameText.getText().toString();
            bruger.Password = mailText.getText().toString();
            phase1 = false;

            nameText.getText().clear();
            mailText.getText().clear();
            nameText.setHint("Name:");
            mailText.setHint("Mail:");
            phoneText.setVisibility(View.VISIBLE);

            createBruger(bruger);
        }
    }

    private void getUsers(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getKunderURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String kdName = jsonObject.getString("navn");
                        getID(id, kdName);
                    }
                } catch (Exception w) {
                    Toast.makeText(Tilmeld.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Tilmeld.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void createKunde(Kunder kunden) {
        StringRequest request = new StringRequest(Request.Method.POST, createKundeURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getUsers();
                try {
                    JSONObject respObj = new JSONObject(response);

                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Tilmeld.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("navn", kunden.name);
                params.put("betalt", String.valueOf(false));
                params.put("mobil", kunden.phone);
                params.put("mail", kunden.mail);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void createBruger(Bruger brugeren) {
        StringRequest request = new StringRequest(Request.Method.POST, createBrugerURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Tilmeld.this, "Bruger oprettet.", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);

                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Tilmeld.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brugernavn", brugeren.brugernavn);
                params.put("password", brugeren.Password);
                params.put("kundeid", String.valueOf(brugeren.brugerId));
                return params;
            }
        };
        requestQueue.add(request);
    }

}