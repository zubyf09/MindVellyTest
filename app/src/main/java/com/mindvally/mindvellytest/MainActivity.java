package com.mindvally.mindvellytest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mindvally.mindvellytest.adapters.PinAdapter;
import com.mindvally.mindvellytest.adapters.SpacesItemDecoration;
import com.mindvally.mindvellytest.interfaces.UserInterface;
import com.mindvally.mindvellytest.model.PinDetails;
import com.mindvally.mindvellytest.service.ServiceGenerator;
import com.mindvally.mindvellytest.utils.C;
import com.mindvally.mindvellytest.utils.Utility;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener ,SwipeRefreshLayout.OnRefreshListener{


    @BindView(R.id.pin_grid)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    PinAdapter adapter;
    Dialog dialog;

    @BindView(R.id.pullToRefresh)

    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initViews();
    }

    public void initViews(){



        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(true);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loadPins();

    }
    public void loadPins(){



        if(!Utility.isInternetAvailable(this))
        {
            Utility.showErrorSnackBar(findViewById(android.R.id.content) , C.ERROR_INTERNET);
            return;
        }



        UserInterface user = ServiceGenerator.createLoginService(UserInterface.class);

        user.getAllPins().enqueue(new Callback<List<PinDetails>>() {
            @Override
            public void onResponse(Call<List<PinDetails>> call, Response<List<PinDetails>> response) {


                if(response!=null)
                {

                        setAdapter(response.body());

                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);

                }
                Log.e("Error","Hello");

            }

            @Override
            public void onFailure(Call<List<PinDetails>> call, Throwable t) {

                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
                Log.e("Error",""+t.getMessage().toString());
            }
        });
    }
    public void setAdapter(List<PinDetails> list){

        adapter = new PinAdapter(this,list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.setAdapter(adapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRefresh()
    {
        loadPins();
    }


//    public void showDialog(Context context)
//    {
//
//        dialog =  new MaterialDialog.Builder(context)
//                .title("Loading")
//                .content("Please wait.....")
//                .progress(true,0)
//                .show();
//    }
//
//    public  void  dissmissDialog()
//    {
//
//        if(dialog!=null)
//        {
//            dialog.dismiss();
//
//        }
//    }
}
