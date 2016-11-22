package com.example.amank.lifeconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import java.util.Date;

/**
 * Created by Jing on 11/21/2016.
 */

public class Appointment_Calendar extends AppCompatActivity {
    private CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_calendar);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setMinDate(new Date().getTime());
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

            }
        });
        Button btnMake = (Button) findViewById(R.id.btnMake);
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}