package salavatorg.salavaltintorg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import salavatorg.salavaltintorg.dao.RequestForSalavat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.salavat_page);

        Log.i(tag,"onCreate");
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        setSupportActionBar(toolbar);

        //---
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width + 20;
        navigationView.setLayoutParams(params);

        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setLanguages(String lan){

        String languageToLoad  = lan; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

//    private void snackerShow(String textMsg) {
//        Snackbar snack = Snackbar.
//                make(coordinatorLayout, textMsg, Snackbar.LENGTH_LONG);
//        View view = snack.getView();CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        view.setLayoutParams(params);
//        snack.show();
//    }


    public void RequestOfSalavatClick(View v){
        startActivity(new Intent(this , RequestForSalavatActivity.class));
    }

    public void ParticipateOfSalavatClick(View v){
        startActivity(new Intent(this , AdvUserInfoActivity.class));
    }

    public void TajliClick(View v){
        startActivity(new Intent(this , TajilInFarajActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
