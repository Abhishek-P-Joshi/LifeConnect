package com.example.amank.lifeconnect;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Abhishek on 11/12/2016.
 */

public class Patient_Main extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView metersMoved;
    private TextView calories;
    private TextView heartbeat;
    private TextView medicines;
    private TextView appointments;
    private static String username;
    private String JsonString;
    private JSONArray patients;
    private String strMedicine, strAppointment, strSteps, strCalories;

    public Patient_Main() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Patient_Main newInstance(int sectionNumber,String s) {
        Patient_Main fragment = new Patient_Main();
        username = s;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_main, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        metersMoved = (TextView) rootView.findViewById(R.id.textView_Moved);
        calories =  (TextView) rootView.findViewById(R.id.textView_Calories);
        heartbeat = (TextView) rootView.findViewById(R.id.textView_heartbeat);
        medicines = (TextView) rootView.findViewById(R.id.textView_medicines);
        appointments = (TextView) rootView.findViewById(R.id.textView_appointments);

        String sql = "SELECT * FROM Patients WHERE Email='"+username+"'";
        GetFromDatabase get = new GetFromDatabase();
        JsonString = get.GetData(sql, FileName.ServerPHP.Patient);
        JSONObject jsonObj = null;
        try
        {
            jsonObj = new JSONObject(JsonString);
            patients = jsonObj.getJSONArray("result");
            JSONObject patient =(JSONObject) patients.get(0);
            strMedicine = patient.getString("medicines");
            medicines.setText(strMedicine);
            strAppointment = patient.getString("appointment");
            appointments.setText(strAppointment);
            strSteps = patient.getString("steps");
            metersMoved.setText(strSteps);
            strCalories = patient.getString("calories");
            calories.setText(strCalories);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        float totalDis = 0;
        float totalCal = 0;
        try{
            File mDatabaseFile = new File(getActivity().getExternalFilesDir(null), "local.db");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
            db.beginTransaction();
            try {
                //perform your database operations here ...
                Cursor c = db.rawQuery("SELECT * FROM " + parseName(username) , null);
                if(c.moveToFirst()){
                    do{
                        //Do something Here with values
                        //inputs1[count] = Float.parseFloat(c.getString((1)));
                        totalDis += Float.parseFloat(c.getString((1)));
                        totalCal += Float.parseFloat(c.getString((2)));
                        //Log.d("my",column2+" "+column3+ " "+ column4+"!!!!");
                    }while(c.moveToNext());
                }

                db.setTransactionSuccessful(); //commit your changes
                c.close();
            }
            catch (SQLiteException e) {
                //report problem
                e.printStackTrace();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){
        }
        totalDis = Float.parseFloat(strSteps) + totalDis;
        totalCal = Float.parseFloat(strCalories) +totalCal;
        metersMoved.setText(totalDis+" Meters");
        calories.setText(totalCal+"");


        /*sql = "SELECT appointment FROM Patients\n" + "WHERE Email='"+username+"';";
        String out = get.GetData(sql,FileName.ServerPHP.Patient);
        //JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(out);
            JSONArray values = jsonObj.getJSONArray("result");
            JSONObject value =(JSONObject) values.get(0);
            String date = value.getString("id");
            appointments.setText(date);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return rootView;
    }

    private String parseName(String username)
    {
        int index = username.indexOf("@");
        if (index!=-1) return username.substring(0,index);
        else return username;
    }
}
