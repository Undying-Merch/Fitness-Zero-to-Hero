package com.example.fitnesszero_to_hero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fitnesszero_to_hero.Classes.Ovelse;
import com.example.fitnesszero_to_hero.Classes.OvelseJunc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonalTrainingOverview extends AppCompatActivity {

    RequestQueue requestQueue;

    private int kdId;
    private int idCount = 1;
    String selectedItem;

    private String getExerciseDate = "http://10.0.2.2:8000/data/ovelseliste/?format=json";
    private String getExerciseJuncData = "http://10.0.2.2:8000/data/otliste/?format=json";

    ArrayList<Ovelse> ovelser = new ArrayList<>();
    ArrayList<OvelseJunc> ovelseJuncs = new ArrayList<>();
    ArrayList<String> personalOvelse = new ArrayList<>();

    private void putIntoOvelser(int id, String ovelseName){
        Ovelse ovelsen = new Ovelse(id, ovelseName);
        ovelser.add(ovelsen);
    }

    private void putIntoOtList(String rep, String mod, int ti, int kd, int ovelse){
        if (kd == kdId){
            for (int i = 0; i < ovelser.size(); i++){
                if (ovelse == ovelser.get(i).id){
                    OvelseJunc ovelseJunc = new OvelseJunc(rep, mod, ti, String.valueOf(idCount) + ": " + ovelser.get(i).name);
                    ovelseJuncs.add(ovelseJunc);
                    personalOvelse.add(String.valueOf(idCount) + ": " + ovelser.get(i).name);
                    idCount++;
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_training_overview);
        requestQueue = Volley.newRequestQueue(this);
        kdId = getIntent().getIntExtra("kundeId", 0);

        int number = 0;
        while (number != 2){
            if (number == 0){
                getOvelser();
            }
            else if (number == 1){
                getOtList();
            }
            number++;
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
                    Toast.makeText(PersonalTrainingOverview.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalTrainingOverview.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getOtList(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getExerciseJuncData, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String repitation = jsonObject.getString("repitation");
                        String modstand = jsonObject.getString("modstand");
                        int tid = jsonObject.getInt("tid");
                        int kundeid = jsonObject.getInt("kundeid");
                        int ovelseid = jsonObject.getInt("ovelseid");
                        putIntoOtList(repitation, modstand, tid, kundeid, ovelseid);
                    }
                } catch (Exception w) {
                    Toast.makeText(PersonalTrainingOverview.this, w.getMessage(), Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalTrainingOverview.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void popUpForChoosingOv (View view){
        TextView rep = (TextView) findViewById(R.id.overviewRepetation);
        TextView mod = (TextView) findViewById(R.id.overviewModstand);
        TextView ti = (TextView) findViewById(R.id.overviewTid);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(PersonalTrainingOverview.this,
                android.R.layout.simple_spinner_item, personalOvelse);
        final Spinner sp = new Spinner(PersonalTrainingOverview.this);
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
                for (int i = 0; i < ovelseJuncs.size(); i++){
                    if (ovelseJuncs.get(i).name.matches(selectedItem)){
                        rep.setText("Repitation: " + ovelseJuncs.get(i).repetition);
                        mod.setText("Modstand: " + ovelseJuncs.get(i).modstand);
                        ti.setText("Tid: " + String.valueOf(ovelseJuncs.get(i).tid));
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalTrainingOverview.this);
        builder.setMessage("Vælg træning");
        builder.setPositiveButton("Ok",dialogListener );
        builder.setView(sp);
        builder.create().show();


    }
}