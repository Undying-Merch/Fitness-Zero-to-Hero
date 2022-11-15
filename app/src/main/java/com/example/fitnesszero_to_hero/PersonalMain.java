package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitnesszero_to_hero.Classes.Kunder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonalMain extends AppCompatActivity {

    private int kundeId;
    private RequestQueue requestQueue;
    private String name;
    private ArrayList<Kunder> kunderne = new ArrayList<>();





    private String kundeURL = "http://10.0.2.2:8000/data/kunde/";
    private String jsonEnding = "/?format=json";
    private String kunderURL = "http://10.0.2.2:8000/data/kundeliste/?format=json";

    private void changeKunderneAtt(int id, String name, String mail, String phone){
        for (int i = 0; i < kunderne.size(); i++){
            if (id == kunderne.get(i).kundeID){
                kunderne.get(i).name = name;
                kunderne.get(i).mail = mail;
                kunderne.get(i).phone = phone;
            }
        }
    }
    private void putIntoList(int id, String name, String mail, String phone, boolean paid){
        Kunder kunde = new Kunder(id, name, phone, mail, paid);
        kunderne.add(kunde);
        if (id == kundeId){
            TextView kundeName = findViewById(R.id.kDName);
            kundeName.setText(name);
        }
    }
    private void setKundeId(int id){
        kundeId = id;
    }
    //TODO Is now under puIntoList (Look at that later)
   /* private void setWelcome(){

        TextView kundeName = findViewById(R.id.kDName);
        for (int i = 0; i < kunderne.size(); i++){
            if (kunderne.get(i).kundeID == kundeId){
                kundeName.setText(kunderne.get(i).name);
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_main);
        setKundeId(getIntent().getIntExtra("KundeId", 100));
        requestQueue = Volley.newRequestQueue(this);
        getKunde(kundeId);
        //Toast.makeText(this, String.valueOf(kunderne.size()), Toast.LENGTH_LONG).show();
    }

    public void gotoPersonalInfo(View view){
        Intent intent = new Intent(PersonalMain.this, PersonalInfo.class);
        intent.putExtra("kundeId", kundeId);
        for (int i = 0; i < kunderne.size(); i++){
            if (kunderne.get(i).kundeID == kundeId){
                intent.putExtra("mail", kunderne.get(i).mail);
                intent.putExtra("phone", kunderne.get(i).phone);
                intent.putExtra("name", kunderne.get(i).name);
                intent.putExtra("active", kunderne.get(i).active);
            }
        }
        startActivity(intent);
    }

    public void gotoTrackTraining(View view){
        Intent intent = new Intent(PersonalMain.this, PersonalTrackTraining.class);
        intent.putExtra("kundeId", kundeId);
        startActivity(intent);
    }

    //TODO make single person
    public void getKunde(int id){
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
                        putIntoList(id, kdName, phone, mail, paid);
                    }
                } catch (Exception w) {
                    Toast.makeText(PersonalMain.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalMain.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}