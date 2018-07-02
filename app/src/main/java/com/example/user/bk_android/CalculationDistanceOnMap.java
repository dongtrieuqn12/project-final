package com.example.user.bk_android;

import android.location.Location;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//public class CalculationDistanceOnMap  {
//}
public class CalculationDistanceOnMap extends AsyncTask<String,Integer,String> {

    StringBuilder stringBuilder;
    Location location1,location2;


    public CalculationDistanceOnMap(Location location1,Location location2){
        this.location1 = location1;
        this.location2 = location2;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();
        try {
            String link = "https://maps.googleapis.com/maps/api/directions/json?origin=" + location1.getLatitude() + "," + location1.getLongitude()+ "&destination="+location2.getLatitude()+","+location2.getLongitude()+"&sensor=false&key=AIzaSyBka9kF8sKdtzpYsrqQ6CIbO-fNUY7QfC8";
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!= null){
                stringBuilder.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
