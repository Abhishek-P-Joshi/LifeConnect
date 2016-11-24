package com.example.amank.lifeconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yogesh on 11/12/2016.
 */

public class SelectPatient extends LoginActivity implements AdapterView.OnItemSelectedListener {
    String stremail,strSelectedPatient;
    private Button btnSelect;
    private Spinner spinPatientList;
    ArrayList<String> PatientList = new ArrayList<String>();
    private String JsonString;
    JSONArray patients = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patient);

        Intent intent = getIntent();
        stremail = intent.getExtras().getString("email");

        spinPatientList = (Spinner)findViewById(R.id.spinnerPatientList);
        spinPatientList.setOnItemSelectedListener(this);
        PatientList = getPatientTableValues();
        final ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, PatientList);
        spinPatientList.setAdapter(my_Adapter);


        btnSelect = (Button)findViewById(R.id.buttonSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPatient.this, DoctorDashboard.class);
                Bundle bu = new Bundle();
                bu.putString("patient", strSelectedPatient);
                intent.putExtras(bu);
                startActivity(intent);
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        strSelectedPatient = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public ArrayList<String> getPatientTableValues() {

        ArrayList<String> my_array = new ArrayList<String>();
        String sql = "SELECT * FROM Doctors WHERE Email='"+stremail+"'";
        GetFromDatabase getFromDatabase = new GetFromDatabase();
        JsonString = getFromDatabase.GetData(sql, FileName.ServerPHP.Doctor);
        showListPatients(JsonString, my_array);
        return my_array;
    }

    protected void showListPatients(String myJSON, ArrayList<String> my_array){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            patients = jsonObj.getJSONArray("result");

            for(int i=0;i<patients.length();i++){
                JSONObject c = patients.getJSONObject(i);
                String Patients = c.getString("patients");
                String []PatientList = Patients.split(",");
                for(int j=0; j<PatientList.length;j++)
                {
                    my_array.add(PatientList[j]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}