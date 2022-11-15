package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitnesszero_to_hero.Classes.Abonnoment;
import com.example.fitnesszero_to_hero.Classes.Bruger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalAbonnement extends AppCompatActivity {

    RequestQueue requestQueue;
    private String allAbonnURL = "http://10.0.2.2:8000/data/abonnementliste/?format=json";
    private String attachAbonnUrl = "http://10.0.2.2:8000/data/opretaj/";
    private String updateAbonnUrl = "http://10.0.2.2:8000/data/aj/";
    private String AllAbonnJun = "http://10.0.2.2:8000/data/ajliste/?format=json";
    private ArrayList<Abonnoment> allAbonn = new ArrayList<>();
    private int abonnId;
    private int kdId;
    private int buttonInt = 1;
    private int junctionId = 0;

    private void putAbonnIntoList(int id, int price, String name, String desc){
        Abonnoment abonn = new Abonnoment(id, price, name, desc);
        allAbonn.add(abonn);
    }
    private void changeCurrentAbonnText(int id, String name){
        TextView currentAbonn = (TextView) findViewById(R.id.textView9);

        if (abonnId == 0){
            currentAbonn.setText("Nuværende: Ingen");
        }
        else if (abonnId == id){
            currentAbonn.setText("Nuværende: " + name);
        }
    }
    private void setBtnText(String name){
        Button btn1 = (Button) findViewById(R.id.button12);
        Button btn2 = (Button) findViewById(R.id.button13);
        Button btn3 = (Button) findViewById(R.id.button14);

        if (buttonInt == 1){
            btn1.setText(name);
        }
        else if (buttonInt == 2){
            btn2.setText(name);
        }
        else if (buttonInt == 3){
            btn3.setText(name);
        }
        buttonInt++;
    }
    private void changeTextFromBtn(String name){
        TextView mainname = (TextView) findViewById(R.id.textView12);
        TextView abonnDesc = (TextView) findViewById(R.id.textView13);


        for (int i = 0; i < allAbonn.size(); i++){
            if (name.matches(allAbonn.get(i).name)){
                mainname.setText(allAbonn.get(i).name);
                abonnDesc.setText(allAbonn.get(i).description);
            }
        }
    }
    private int getId(){
        int x = 0;
        TextView theText = (TextView) findViewById(R.id.textView12);
        String abonnText = theText.getText().toString();
        for (int i = 0; i < allAbonn.size(); i++){
            if (allAbonn.get(i).name.matches(abonnText)){
                x = allAbonn.get(i).abonId;
            }
        }
        return x;
    }
    private void setJunctionId(int kunId, int junId){
        if (kunId == kdId){
            junctionId = junId;
        }
    }

    public void setBtn1(View view){
        Button btn = (Button) findViewById(R.id.button12);
        String btnName = btn.getText().toString();
        changeTextFromBtn(btnName);

    }
    public void setBtn2(View view){
        Button btn = (Button) findViewById(R.id.button13);
        String btnName = btn.getText().toString();
        changeTextFromBtn(btnName);

    }
    public void setBtn3(View view){
        Button btn = (Button) findViewById(R.id.button14);
        String btnName = btn.getText().toString();
        changeTextFromBtn(btnName);

    }
    public void createOrUpdate(View view){
        TextView theText = (TextView) findViewById(R.id.textView12);
        String text = theText.getText().toString();
        if (abonnId == 0){
            attachAbonn(getId());
        }
        else{
            updateAbonn(getId());

        }
        for (int i = 0; i < allAbonn.size(); i++){
            if (allAbonn.get(i).name.matches(text));{
                abonnId = allAbonn.get(i).abonId;
            }
        }
        changeCurrentAbonnText(abonnId, text);
        saveButton();
    }
    private void saveButton(){
        Button SaveButton = (Button) findViewById(R.id.button15);
        if (abonnId == 0){
            SaveButton.setText("Sæt Abonnoment.");
        }
        else{
            SaveButton.setText("Ændre Abonnoment");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_abonnement);
        requestQueue = Volley.newRequestQueue(this);

        abonnId = getIntent().getIntExtra("abonnId", 0);
        kdId = getIntent().getIntExtra("kundeId", 0);


        getAllAbonn();
        getAbonnJunction();

        saveButton();
    }



    private void getAllAbonn(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, allAbonnURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        int price = jsonObject.getInt("price");
                        String name = jsonObject.getString("navn");
                        String desc = jsonObject.getString("beskrivelse");
                        putAbonnIntoList(id, price, name, desc);
                        changeCurrentAbonnText(id, name);
                        setBtnText(name);
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

    private void attachAbonn(int x) {
        StringRequest request = new StringRequest(Request.Method.POST, attachAbonnUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PersonalAbonnement.this, "Abonnoment tilføjet", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);

                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalAbonnement.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kundeid", String.valueOf(kdId));
                params.put("abonnementid", String.valueOf(x));
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void updateAbonn(int x){
        StringRequest request = new StringRequest(Request.Method.PUT, updateAbonnUrl + junctionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PersonalAbonnement.this, "Abonnoment ændret", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalAbonnement.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kundeid", String.valueOf(kdId));
                params.put("abonnementid", String.valueOf(x));
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void getAbonnJunction(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, AllAbonnJun, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        int kundeId = jsonObject.getInt("kundeid");
                        setJunctionId(kundeId, id);
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