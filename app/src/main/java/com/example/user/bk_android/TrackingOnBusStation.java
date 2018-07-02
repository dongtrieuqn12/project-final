package com.example.user.bk_android;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bk_android.Adapter.SampleFragmentPagerAdapter;
import com.example.user.bk_android.fragment.OneFragment;
import com.example.user.bk_android.fragment.PageFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class TrackingOnBusStation extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView im_back2;
    SampleFragmentPagerAdapter sampleFragmentPagerAdapter;
    static int check = 0;
    Button btn_reload;
    TextView tv_header_titles;
    PageFragment pageFragment;

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_on_bus_station);

        pageFragment = new PageFragment();
        initView();
        initClick();
        //String name = getIntent().getStringExtra("header");
        tv_header_titles.setText(getIntent().getStringExtra("header"));
        sampleFragmentPagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        sampleFragmentPagerAdapter.addFragment(new PageFragment(),"THỜI GIAN CHỜ");
        sampleFragmentPagerAdapter.addFragment(new OneFragment(),"CÁC TUYẾN BUÝT");
        viewPager.setAdapter(sampleFragmentPagerAdapter);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView(){
        viewPager = findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tab);
        im_back2 = findViewById(R.id.im_back2);
        btn_reload = findViewById(R.id.btn_reload);
        tv_header_titles = findViewById(R.id.tv_header_titles);
    }
    private void initClick(){
        im_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = getIntent().getStringExtra("activity");
                pageFragment.CancelRefresh();

                switch (temp){
                    case "ChiTietChuyenXe":
                    {
                        Intent intent = new Intent(TrackingOnBusStation.this,ChiTietChuyenXe.class);
                        startActivity(intent);
                        TrackingOnBusStation.this.finish();
                    } break;

                    case "MainActivity":
                    {
                        Intent intent = new Intent(TrackingOnBusStation.this,MainActivity.class);
                        startActivity(intent);
                        TrackingOnBusStation.this.finish();
                    } break;

                    case "LocationOfBus" :
                    {
                        Intent intent = new Intent(TrackingOnBusStation.this,ChiTietChuyenXe.class);
                        startActivity(intent);
                        TrackingOnBusStation.this.finish();
                    } break;
                }

            }
        });
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    public int getData(){
        int i = getIntent().getIntExtra("id",0);
        return i;
    }

    public String getHeader(){
        String s = getIntent().getStringExtra("header");
        return s;
    }

}
