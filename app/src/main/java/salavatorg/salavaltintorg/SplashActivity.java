package salavatorg.salavaltintorg;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import salavatorg.salavaltintorg.dao.AppCreatorDatabase;
import salavatorg.salavaltintorg.dao.UserInfoBase;

/**
 * Created by masoomeh on 1/13/18.
 */

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private AppCreatorDatabase db;
    String tag = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_acctivity);
        db = Room.databaseBuilder(
                SplashActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();


          /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                getDataUser();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void getDataUser() {
        new AsyncTask<Void, Void, List<UserInfoBase>>() {
            @Override
            protected List<UserInfoBase> doInBackground(Void... voids) {
                 List<UserInfoBase> userInfoBases = db.userInfoBaseDao().fetchAllData();
                return userInfoBases;
            }

            @Override
            protected void onPostExecute(List<UserInfoBase> userInfoBases) {
                super.onPostExecute(userInfoBases);
                    Log.e(tag,"MainActivity:size: " + userInfoBases.size());
//                Log.e("data" ,"number: "+"phone_num"+ userInfoBases.get(0).getName());
                if(userInfoBases.size()==1){
                    if(userInfoBases.get(0).getPassword() != null && !userInfoBases.get(0).getPassword().isEmpty() ){
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        Log.e("data" ,"number: "+"phone_num"+ userInfoBases.get(0).getPhone_num()
                                +"name: "+ userInfoBases.get(0).getName() +  userInfoBases.get(0).getFamily()
                        + userInfoBases.get(0).getId());
                        mainIntent.putExtra("phone_num", userInfoBases.get(0).getPhone_num());
                        mainIntent.putExtra("user_id", String.valueOf(userInfoBases.get(0).getId()));
                        mainIntent.putExtra("name", userInfoBases.get(0).getName());
                        mainIntent.putExtra("family", userInfoBases.get(0).getFamily());
                        SplashActivity.this.startActivity(mainIntent);
                    }else {
                        Intent mainIntent = new Intent(SplashActivity.this, PasswordActivity.class);
                        Log.e("data" ,"number: "+"phone_num"+ userInfoBases.get(0).getPhone_num()
                        +"name: "+ userInfoBases.get(0).getName());
                        mainIntent.putExtra("phone_num", userInfoBases.get(0).getPhone_num());
                        mainIntent.putExtra("user_id", userInfoBases.get(0).getId());
                        mainIntent.putExtra("name", userInfoBases.get(0).getName());
                        mainIntent.putExtra("family", userInfoBases.get(0).getFamily());
                        SplashActivity.this.startActivity(mainIntent);
                    }

                }else {
                    Log.e(tag,"LoginActivity");
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                }
                SplashActivity.this.finish();
            }
        }.execute();
    }
}
