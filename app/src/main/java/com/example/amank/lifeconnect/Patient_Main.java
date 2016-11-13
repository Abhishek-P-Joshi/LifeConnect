package com.example.amank.lifeconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Abhishek on 11/12/2016.
 */

public class Patient_Main extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView numberOfSteps;
    private TextView calories;
    private TextView heartbeat;
    private TextView medicines;
    private TextView appointments;

    public Patient_Main() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Patient_Main newInstance(int sectionNumber) {
        Patient_Main fragment = new Patient_Main();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_main, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        numberOfSteps = (TextView) rootView.findViewById(R.id.textView_Steps);
        calories =  (TextView) rootView.findViewById(R.id.textView_Calories);
        heartbeat = (TextView) rootView.findViewById(R.id.textView_heartbeat);
        medicines = (TextView) rootView.findViewById(R.id.textView_medicines);
        appointments = (TextView) rootView.findViewById(R.id.textView_appointments);

        return rootView;
    }
}
