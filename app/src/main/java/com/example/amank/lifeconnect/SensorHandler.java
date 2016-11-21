package com.example.amank.lifeconnect;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.location.LocationListener;
import android.location.Location;
import android.os.Bundle;
import android.location.LocationManager;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by Jing on 11/14/2016.
 */

public class SensorHandler extends Service implements SensorEventListener, LocationListener{
    LocationManager locationManager;
    Location current;
    MyTimerTask timerTask;
    SensorTask sensorTask;
    Timer timer;
    private SensorManager accelManage;
    private Sensor senseAccel;
    float accelValuesX[] = new float[10];
    float accelValuesY[] = new float[10];
    float accelValuesZ[] = new float[10];
    int index = 0;
    private static final int GPS_TIME_INTERVAL = 2000; // get gps location every 2 seconds
    private static final int GPS_DISTANCE= 1; // set the distance value in meter
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(index<10) {
                index++;
            }
            else
            {
                index = 0;
                float sum = 0;
                for(int i=0;i<10;i++)
                {
                    sum = sum + Math.abs(accelValuesX[i]) + Math.abs(accelValuesY[i]) + Math.abs(accelValuesZ[i]);
                }
                if(sum<140) timerTask.updateWalkOrRun(true);
                else timerTask.updateWalkOrRun(false);
            }
            accelValuesX[index] = sensorEvent.values[0];
            accelValuesY[index] = sensorEvent.values[1];
            accelValuesZ[index] = sensorEvent.values[2];
            accelManage.unregisterListener(this);
            //accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location loc) {
        Toast.makeText(
                getBaseContext(),
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        String longitude = "Longitude: " + loc.getLongitude();
        String latitude = "Latitude: " + loc.getLatitude();
        current = new Location("current");
        current.set(loc);
        timerTask.updateLocation(current);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onCreate(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        current = new Location("current");
        accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, this);
        }catch (SecurityException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 10000, 10000);

        sensorTask = new SensorTask(accelManage,senseAccel,this);
        timer.scheduleAtFixedRate(sensorTask, 1000, 1000);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    class MyTimerTask extends TimerTask
    {
        Location current;
        Location previous;
        boolean first;
        boolean walkOrRun;
        public MyTimerTask() {
            previous = new Location("previous");
            current = new Location("current");
            first =true;
            walkOrRun = false;
        }
        public void updateLocation(Location loc)
        {
            if(first){
                previous.set(loc);
                first = false;
            }
            current.set(loc);
        }

        public void updateWalkOrRun(boolean b)
        {
            walkOrRun = b;
        }

        @Override
        public void run() {
            double distance = current.distanceTo(previous);
            String info = "current loc:"+ current.getLatitude()+"\nprevious loc:"+previous.getLatitude()+"\ndistance moved:"+distance;
            previous.set(current);
            Log.d(TAG, info);
            if(walkOrRun && distance < 120)
            {
                //insert into database
            }
        }
    }
    class SensorTask extends TimerTask
    {
        private SensorManager accelManage;
        private Sensor senseAccel;
        SensorEventListener listener;
        public SensorTask(SensorManager sm, Sensor s, SensorEventListener li) {
            accelManage = sm;
            senseAccel = s;
            listener = li;
        }
        @Override
        public void run() {
            accelManage.registerListener(listener, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
