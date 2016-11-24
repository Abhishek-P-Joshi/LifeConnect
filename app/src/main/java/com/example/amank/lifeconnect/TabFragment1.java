package com.example.amank.lifeconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yogesh on 11/13/2016.
 */

public class TabFragment1 extends Fragment {

    private String strDistance, strCalories, strHeartBeat, strMedicine, strAppointment;
    private String strPatientName;
    private TextView txtMoved;
    private TextView txtCalories;
    private TextView txtHeartBeats;
    private TextView txtMedicines;
    private TextView txtAppointment;
    private String JsonString;
    JSONArray patients = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_1, container, false);
        strPatientName = getArguments().getString("Patient Name");

        txtMoved = (TextView)v.findViewById(R.id.textView_Moved);
        txtCalories = (TextView)v.findViewById(R.id.textView_Calories);
        txtHeartBeats = (TextView)v.findViewById(R.id.textView_heartbeat);
        txtMedicines = (TextView)v.findViewById(R.id.textView_medicines);
        txtAppointment = (TextView)v.findViewById(R.id.textView_appointments);

        String sql = "SELECT * FROM Patients WHERE Name='"+strPatientName+"'";
        GetFromDatabase get = new GetFromDatabase();
        JsonString = get.GetData(sql, FileName.ServerPHP.Patient);
        JSONObject jsonObj = null;
        try
        {
            jsonObj = new JSONObject(JsonString);
            patients = jsonObj.getJSONArray("result");
            JSONObject patient =(JSONObject) patients.get(0);
            strDistance = patient.getString("steps");
            strCalories = patient.getString("calories");
            strHeartBeat = patient.getString("heartbeat");
            strMedicine = patient.getString("medicines");
            strAppointment = patient.getString("appointment");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        txtMoved.setText(strDistance+" meters");
        txtCalories.setText(strCalories);
        txtHeartBeats.setText(strHeartBeat);
        txtMedicines.setText(strMedicine);
        txtAppointment.setText(strAppointment);
        return v;
    }
}