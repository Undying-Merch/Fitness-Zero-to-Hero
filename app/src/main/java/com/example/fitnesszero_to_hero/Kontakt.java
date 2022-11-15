package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Kontakt extends AppCompatActivity {

    private String mailUrl = "http://10.0.2.2:8000/data/mail/";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontakt);

        requestQueue = Volley.newRequestQueue(this);

    }

    public void sendMessage(View view){
        EditText nameText = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText mailText = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText messageText = (EditText) findViewById(R.id.editTextTextMultiLine);

        String name = nameText.getText().toString();
        String mail = mailText.getText().toString();
        String message = messageText.getText().toString();

        sendMessageData(name, mail, message);



    }

    public void clearFields(){
        EditText nameText = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText mailText = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText messageText = (EditText) findViewById(R.id.editTextTextMultiLine);
        nameText.getText().clear();
        mailText.getText().clear();
        messageText.getText().clear();
    }

    private void sendMessageData(String name, String mail, String message) {
        StringRequest request = new StringRequest(Request.Method.POST, mailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Kontakt.this, "Besked sendt", Toast.LENGTH_SHORT).show();
                clearFields();
                try {
                    JSONObject respObj = new JSONObject(response);

                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Kontakt.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("navn", name);
                params.put("email", mail);
                params.put("besked", message);
                return params;
            }
        };
        requestQueue.add(request);
    }
}