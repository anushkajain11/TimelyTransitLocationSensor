package com.example.a5750_9864.locationsensor;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5750-9864 on 02/05/2016.
 */
public class SampleActivity extends Activity {


    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    protected LocationManager locationManager;

    protected Button retrieveLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        //================================
        retrieveLocationButton = (Button) findViewById(R.id.ok);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_FINE_LOCATION );
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );

        retrieveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCurrentLocation();
            }
        });

    }

    protected void showCurrentLocation() {

        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch(SecurityException e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(this, message,
                    Toast.LENGTH_LONG).show();
        }
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost("http://miniproject11.mybluemix.net/pushlocation");

        long time= System.currentTimeMillis();
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("TIME", ""+time));

        nameValuePair.add(new BasicNameValuePair("LATITUDE", ""+location.getLatitude()));
        nameValuePair.add(new BasicNameValuePair("LONGITUDE", ""+location.getLongitude()));


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (Exception e) {
            // Log exception
            //Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

        }



    }
    //================================


    public void startAlert(View view) {
        //EditText text = (EditText) findViewById(R.id.time);
        //int i = Integer.parseInt(text.getText().toString());
        //Intent intent = new Intent(this, MyBroadCastReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        /*AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);*/



       /* try{

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LocationManager.requestLocationUpdates(

                    LocationManager.GPS_PROVIDER,

                    5,

                    25,

            new MyLocationListener()

            );


            Toast.makeText(this,"Latitude: " + loc.getLatitude() + "\nLongitude: " +
                    loc.getLongitude(),Toast.LENGTH_LONG).show();

           // Toast.makeText(this," net connectivity is " +
            //        isOnline() ,Toast.LENGTH_LONG).show();

            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://miniproject11.mybluemix.net/pushlocation");


            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("LATITUDE", ""+loc.getLatitude()));
            nameValuePair.add(new BasicNameValuePair("LONGITUDE", ""+loc.getLongitude()));


            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
            } catch (Exception e) {
                // Log exception
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

            }


        }
        catch(SecurityException e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Alarm set in 10 seconds",
                Toast.LENGTH_LONG).show();*/
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}
class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {


            String message = String.format(

                    "New Location \n Longitude: %1$s \n Latitude: %2$s",

                    location.getLongitude(), location.getLatitude()

            );
            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://miniproject11.mybluemix.net/pushlocation");

        long time= System.currentTimeMillis();
            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("TIME", ""+time));

        nameValuePair.add(new BasicNameValuePair("LATITUDE", ""+location.getLatitude()));
            nameValuePair.add(new BasicNameValuePair("LONGITUDE", ""+location.getLongitude()));


            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
            } catch (Exception e) {
                // Log exception
                //Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

            }


            // Toast.makeText(this, message, Toast.LENGTH_LONG).show();



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
