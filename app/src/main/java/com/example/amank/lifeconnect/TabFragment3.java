package com.example.amank.lifeconnect;

/**
 * Created by Yogesh on 11/13/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment3 extends Fragment {

    private String strPatientName, strDoctorName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.tab_fragment_3, container, false);
        strPatientName = getArguments().getString("Patient Name");
        strDoctorName = getArguments().getString("doctorName");

        return v;
    }
}