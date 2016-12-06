package com.example.amank.lifeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Abhishek on 11/12/2016.
 */

public class Patient_Chat extends android.support.v4.app.Fragment {

    public static Patient_Chat newInstance() {
        Patient_Chat fragment = new Patient_Chat();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_chat, container, false);
        return rootView;
    }
}
