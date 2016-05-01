package com.example.brandon.defachelleclouetbrandon_2916832_ass3_journeytracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private Button OnTracking;
    private HashMap<String, TextView> map;
    private GPSView GPS;
    private LocationManager locationManager;
    public static CircularQueue queue;
    private ImageView imageView;
    boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OnTracking = (Button) findViewById(R.id.track);
        GPS = (GPSView) findViewById(R.id.GPS);
        queue = new CircularQueue();
        map = new HashMap<>();
        imageView = (ImageView)findViewById(R.id.imageView);
        isActive=false;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                if(isActive)
                {
                    queue.add(location);
                    //TextView Update
                    map.get("average").setText(getString(R.string.average) + queue.getAverage() + " km/s");
                    map.get("curr").setText(getString(R.string.speed) + queue.getSpeed() + " km/s");
                    map.get("overall").setText(getString(R.string.overall) + PrintOverallTime((int)queue.getTime()));     //MINUTE, HEURES ETC.
                    //PRINT NEW graph()
                    GPS.invalidate();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
        catch(SecurityException e)
        {
            // requires ACCESS_FINE_LOCATION permission
            Log.e("LOCATION", e.getMessage());
        }

        map.put("overall", (TextView)findViewById(R.id.overallTime));
        map.put("curr", (TextView)findViewById(R.id.currSpeed));
        map.put("average", (TextView)findViewById(R.id.averageSpeed));
        map.put("active", (TextView)findViewById(R.id.gps_active));

        OnTracking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isActive)
                {
                    OnTracking.setText(getString(R.string.start_tracking));
                    map.get("active").setText(getString(R.string.gps_inactive));
                    map.get("average").setText(getString(R.string.average)+ " N/A");
                    map.get("curr").setText(getString(R.string.speed)+ " N/A");
                    map.get("overall").setText(getString(R.string.overall) + " 0s");
                    queue.reset();
                }
                else
                {
                    OnTracking.setText(getString(R.string.stop_tracking));
                   map.get("active").setText(getString(R.string.gps_active));
                   map.get("average").setText(getString(R.string.average) + "0s");
                   map.get("curr").setText(getString(R.string.speed) + " 0s");
                    map.get("overall").setText(getString(R.string.overall) + " 0s");
                }
                 isActive=!isActive; //Switch On to Off, and inversely.
                 GPS.TurnOnOff();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String PrintOverallTime(int time)
    {
        String strTime="";
        int day=86400;
        int hour = 3600;
        int min = 60;
        if(time>=day)
        {
            strTime +=  " " + String.valueOf(time/day) + " d";
            time = time % day;
        }
        if(time >= hour)
        {
            strTime +=  " " + String.valueOf(time/hour) + " h";
            time = time % hour;
        }
        if(time >= min)
        {
            strTime += " " +  String.valueOf(time/min) + " m";
            time = time % min;
        }
        if(time!=0)
        {
            strTime += " " +  String.valueOf(time) + " s";
        }
        return strTime + ".";
    }


}
