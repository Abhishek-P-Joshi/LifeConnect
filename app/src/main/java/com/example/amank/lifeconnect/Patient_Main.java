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
import android.widget.Toast;
import android.content.Context;

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

        float totalDis = 0;
        float totalCal = 0;
        try{
            File mDatabaseFile = new File(getActivity().getExternalFilesDir(null), "local.db");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
            db.beginTransaction();
            try {
                //perform your database operations here ...
                Cursor c = db.rawQuery("SELECT * FROM " + username , null);
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
        metersMoved.setText(totalDis+" Meters");
        calories.setText(totalCal+"");

        return rootView;
    }
}
