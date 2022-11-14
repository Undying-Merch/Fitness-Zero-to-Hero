package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitnesszero_to_hero.Classes.Abonnoment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonalAbonnement extends AppCompatActivity {

    RequestQueue requestQueue;
    private String allAbonnURL = "http://10.0.2.2:8000/data/abonnementliste/?format=json";
    private ArrayList<Abonnoment> allAbonn = new ArrayList<>();
    private int abonnId;
    private int kdId;

    private void putAbonnIntoList(int price, String name, String desc){
        Abonnoment abonn = new Abonnoment(price, name, desc);
        allAbonn.add(abonn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_abonnement);
        requestQueue = Volley.newRequestQueue(this);

        abonnId = getIntent().getIntExtra("abonnId", 0);
        kdId = getIntent().getIntExtra("kundeId", 0);

        getAllAbonn();

        Toast.makeText(this, String.valueOf(abonnId), Toast.LENGTH_SHORT).show();
    }



    private void getAllAbonn(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, allAbonnURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int price = jsonObject.getInt("price");
                        String name = jsonObject.getString("navn");
                        String desc = jsonObject.getString("beskrivelse");
                        putAbonnIntoList(price, name, desc);
                    }
                } catch (Exception w) {
                    Toast.makeText(PersonalAbonnement.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalAbonnement.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


}