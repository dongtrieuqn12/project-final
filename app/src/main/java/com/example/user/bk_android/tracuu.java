package com.example.user.bk_android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.user.bk_android.Adapter.ContactsAdapter;
import com.example.user.bk_android.TableList.Table;

import java.util.ArrayList;
import java.util.List;

public class tracuu extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Table> tableList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;

    int[] image = {R.drawable.number1,R.drawable.number2,R.drawable.number8,R.drawable.number19,R.drawable.number33,R.drawable.number50,R.drawable.number66,R.drawable.number94};
    String[] string1 = {"Tuyến số 01","Tuyến số 02","Tuyến số 08","Tuyến số 19","Tuyến số 33","Tuyến số 50","Tuyến số 66","Tuyến số 94"};
    String[] string2 = {"Bến Thành - BX Chợ Lớn","Bến Thành - BX Miền Tây","Bến xe Quận 8 - Đại học Quốc Gia","Bến Thành - Khu Chế Xuất Linh Trung","Bến xe An Sương - Suối Tiên - Đại học QG","Đại học Bách Khoa-Đại học QG","Bến xe Chợ Lớn-Bến xe An Sương","Bến xe Chợ Lớn-Bến xe Củ Chi"};
    //List<Table> tableList;
    ImageView im_backtracuu;
    // url to fetch contacts json
    private static final String URL = "https://api.androidhive.info/json/contacts.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracuu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recycler_view);
        tableList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, tableList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        fetchContacts();

        im_backtracuu = findViewById(R.id.im_backtracuu);
        im_backtracuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tracuu.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * fetches json by making http calls
     */
    private void fetchContacts() {
        tableList.clear();
        for(int i = 0;i<8;i++){
            Table table = new Table();
            table.setImage(image[i]);
            Log.d("trieu",string1[i]);
            table.setString1(string1[i]);
            table.setString2(string2[i]);
            tableList.add(table);
        }

        //MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    @Override
    public void onContactSelected(Table table) {
        if(table.getString1().equals("Tuyến số 50")) {
            Intent intent = new Intent(tracuu.this,ChiTietChuyenXe.class);
            Bundle bundle = new Bundle();
            bundle.putString("NameTuyen","Tuyến số 50");
            bundle.putString("Name50",table.getString2());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}