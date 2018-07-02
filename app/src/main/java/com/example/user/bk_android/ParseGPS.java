package com.example.user.bk_android;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ParseGPS extends AsyncTask<String,Integer,String> {


    StringBuilder stringBuilder;
    public ParseGPS(){

    }
    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + strings[0]+"&key=AIzaSyBka9kF8sKdtzpYsrqQ6CIbO-fNUY7QfC8");
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
