package com.example.amank.lifeconnect;

/**
 * Created by Yogesh on 11/13/2016.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String strPatientName;
    String strDoctorName;

    Bundle bundle = new Bundle();

    public PagerAdapter(FragmentManager fm, int NumOfTabs, String strName, String doctorName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.strPatientName=strName;
        this.strDoctorName=doctorName;

        bundle.putString("Patient Name",strPatientName);
        bundle.putString("doctorName", strDoctorName);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                tab3.setArguments(bundle);
                return tab3;
            case 3:
                TabFragment4 tab4 = new TabFragment4();
                tab4.setArguments(bundle);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}