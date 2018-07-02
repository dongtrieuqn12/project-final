package com.example.user.bk_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {


    GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    LinearLayout hide,viewPager;
    ImageView im_bus,im_menu;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    public static final int REQUEST_PERMISSION_LOCATION = 1;
    Location vitrihientai;
    Button btn_timkiem;
    boolean flag = false;
    EditText diachi;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    android.support.v7.widget.Toolbar toolbar;
    ActionBar actionBar;
    int check = 0;
    ArrayList<Integer> id;
    ArrayList<Double> lat,lng;
    ArrayList<String> tentram;
    ArrayList<Marker> markerPoints;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        int checkPermisson = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(checkPermisson != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_LOCATION);
        }else{
            googleApiClient.connect();
        }



        init_layout();
        click_action();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerToggle.syncState();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    googleApiClient.connect();
                }break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(check == 0) {
            LatLng toado = new LatLng(vitrihientai.getLatitude(), vitrihientai.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(toado);
            Marker KTX = mMap.addMarker(markerOptions);
            KTX.setTag(0);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(toado).zoom(15).bearing(90).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            try {
                FindNear(vitrihientai);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Log.d("checkIDofthis",check +"");
            mMap.clear();
            id.clear();
            lat.clear();
            lng.clear();
            tentram.clear();
            LatLng toado = new LatLng(vitrihientai.getLatitude(), vitrihientai.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(toado);
            mMap.addMarker(markerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(toado).zoom(15).bearing(90).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            check = 0;
            try {
                FindNear(vitrihientai);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MainActivity.this,marker.getTitle(),Toast.LENGTH_LONG).show();
                String s = marker.getTitle();
                for(int i = 0; i < tentram.size() ; i++){
                    if(tentram.get(i).equals(s)){
                        Intent intent = new Intent(MainActivity.this,TrackingOnBusStation.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("activity","MainActivity");
                        bundle.putInt("id",id.get(i));
                        bundle.putString("header",tentram.get(i));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else {
                        //do nothing
                    }
                }
            }
        });

    }

    private void init_layout(){
        hide = findViewById(R.id.hide);
        im_bus = findViewById(R.id.im_bus);
        btn_timkiem = findViewById(R.id.btn_timkiem);
        hide.setVisibility(View.GONE);
        diachi = findViewById(R.id.diachi);
        viewPager = findViewById(R.id.viewPager);
        im_menu = findViewById(R.id.im_menu);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        id = new ArrayList<>();
        lat = new ArrayList<>();
        lng = new ArrayList<>();
        tentram = new ArrayList<>();
        markerPoints = new ArrayList<Marker>();
    }

    private void click_action(){

        im_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        im_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,tracuu.class);
                startActivity(intent);
            }
        });

        btn_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    if(diachi.getText().toString().equals("")) {
                        //do nothing
                    }else{
                        Timkiemdiadiem(diachi.getText().toString());
                        diachi.setText("");
                    }
                    TranslateAnimation animation = new TranslateAnimation(
                            0,
                            0,
                            0,
                            viewPager.getHeight() + hide.getHeight()
                    );
                    animation.setDuration(1000);
                    animation.setFillAfter(true);
                    hide.startAnimation(animation);
                    hide.setVisibility(View.GONE);
                }else{
                    flag = true;
                    TranslateAnimation animation = new TranslateAnimation(
                            0,
                            0,
                            viewPager.getHeight(),
                            0
                    );
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    hide.startAnimation(animation);
                    hide.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        vitrihientai = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void Timkiemdiadiem(String s){
        ParseGPS parseGPS = new ParseGPS();
        parseGPS.execute(s);
        try {
            String s1 = parseGPS.get();
            Log.d("hello",s1);
            JSONObject jsonObject = new JSONObject(s1);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
            vitrihientai.setLatitude(jsonObject3.getDouble("lat"));
            vitrihientai.setLongitude(jsonObject3.getDouble("lng"));
            Log.d("kiemtra1",vitrihientai.getLatitude()+"");
            check = 1;
            mMap.clear();
            supportMapFragment.getMapAsync(MainActivity.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void FindNear(Location location) throws ExecutionException, InterruptedException, JSONException {
        FindBusStation findBusStation = new FindBusStation(location);
        findBusStation.execute();
        String s = findBusStation.get();
        JSONArray jsonArray = new JSONArray(s);
        for(int i = 0 ; i < jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            id.add(jsonObject.getInt("id"));
            lat.add(jsonObject.getDouble("latitude"));
            lng.add(jsonObject.getDouble("longitude"));
            tentram.add(jsonObject.getString("tentram"));
        }
        for (int i = 0; i < tentram.size() ; i++){
            LatLng toado = new LatLng(lat.get(i),lng.get(i));
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.busstop);
            MarkerOptions option = new MarkerOptions().position(toado).icon(icon).title(tentram.get(i));
            Marker currentMarker = mMap.addMarker(option);
            markerPoints.add(currentMarker);
            currentMarker.showInfoWindow();

        }
    }

}
