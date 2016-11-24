package com.example.amank.lifeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jing on 11/21/2016.
 */

public class Appointment_Calendar extends Fragment {
    private CalendarView calendar;
    private static String email;
    private Date date;

    public static Appointment_Calendar newInstance(String s) {
        Appointment_Calendar fragment = new Appointment_Calendar();
        email = s;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appointment_calendar, container, false);
        calendar = (CalendarView) rootView.findViewById(R.id.calendarView);
        calendar.setMinDate(new Date().getTime());
        calendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = new GregorianCalendar(year, month, dayOfMonth).getTime();
            }//met
        });
        Button btnMake = (Button) rootView.findViewById(R.id.btnMake);
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.sql.Date d = new java.sql.Date(date.getTime());
                GetFromDatabase update= new GetFromDatabase();
                String sql = "UPDATE Patients\n" +"SET appointment='"+d+"'\n" +"WHERE Email='"+email+"';";
                update.GetData(sql,FileName.ServerPHP.Patient);
            }
        });
        return rootView;
    }
}