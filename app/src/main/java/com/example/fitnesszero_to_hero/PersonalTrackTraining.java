package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitnesszero_to_hero.Classes.Bruger;
import com.example.fitnesszero_to_hero.Classes.Ovelse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonalTrackTraining extends AppCompatActivity {

    //TODO Ingen failsafe, kan sende NULL data ind
    RequestQueue requestQueue;
    private String getExerciseDate = "http://10.0.2.2:8000/data/ovelseliste/?format=json";
    private String createTrainingUrl = "http://10.0.2.2:8000/data/opretot/";

    int kdId;
    int ovelsesId = 0;
    String selectedItem;

    private ArrayList<Ovelse> ovelser = new ArrayList<>();
    private ArrayList<String> ovelseListe = new ArrayList<>();


    private void putIntoOvelser(int id, String ovelseName){
        Ovelse ovelsen = new Ovelse(id, ovelseName);
        ovelser.add(ovelsen);
        ovelseListe.add(ovelseName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_track_training);
        kdId = getIntent().getIntExtra("kundeId", 0);

        TextView ovelsesDisplay = (TextView) findViewById(R.id.textView15);

        requestQueue = Volley.newRequestQueue(this);
        getOvelser();



    }

    public void createTrainingBtn(View view){
        EditText ovelseRepitation = (EditText) findViewById(R.id.editTextTextPersonName5);
        EditText ovelseModstand = (EditText) findViewById(R.id.editTextTextPersonName6);
        EditText ovelseTid = (EditText) findViewById(R.id.editTextNumber2);

        String repetation = ovelseRepitation.getText().toString();
        String modstand = ovelseModstand.getText().toString();
        String tid = ovelseTid.getText().toString();

        if (ovelsesId == 0){
            Toast.makeText(this, "Vælg venligst en from for træning", Toast.LENGTH_SHORT).show();
        }
        else {
            createTraining(repetation, modstand, tid);
            ovelseRepitation.getText().clear();
            ovelseModstand.getText().clear();
            ovelseTid.getText().clear();
        }

    }

    private void getOvelser(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getExerciseDate, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String ovelseName = jsonObject.getString("navn");
                        putIntoOvelser(id, ovelseName);
                    }
                } catch (Exception w) {
                    Toast.makeText(PersonalTrackTraining.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalTrackTraining.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void createTraining(String rep, String mod, String tid) {
        StringRequest request = new StringRequest(Request.Method.POST, createTrainingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PersonalTrackTraining.this, "Træning oprettet.", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);

                } catch (JSONException jE) {
                    jE.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalTrackTraining.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("repitation", rep);
                params.put("modstand", mod);
                params.put("tid", tid);
                params.put("kundeid", String.valueOf(kdId));
                params.put("ovelseid", String.valueOf(ovelsesId));
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void popUpForChoosing (View view){
        TextView ovelsee = (TextView) findViewById(R.id.textView15);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(PersonalTrackTraining.this,
                android.R.layout.simple_spinner_item, ovelseListe);
        final Spinner sp = new Spinner(PersonalTrackTraining.this);
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = adp.getItem(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < ovelser.size(); i++){
                    if (ovelser.get(i).name.matches(selectedItem)){
                        ovelsesId = ovelser.get(i).id;
                        ovelsee.setText(selectedItem);
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalTrackTraining.this);
        builder.setMessage("Vælg træning");
        builder.setPositiveButton("Ok",dialogListener );
        builder.setView(sp);
        builder.create().show();


    }
}