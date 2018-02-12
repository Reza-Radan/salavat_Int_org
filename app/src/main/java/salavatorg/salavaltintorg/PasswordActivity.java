package salavatorg.salavaltintorg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavatorg.salavaltintorg.dao.JsonParser;

/**
 * Created by masoomeh on 12/14/17.
 */

public class PasswordActivity extends AppCompatActivity{
    @BindView(R.id.input_password)
    EditText password;

    @BindView(R.id.avloadingIndicatorViewResult)
    AVLoadingIndicatorView Loading;

    @BindView(R.id.btncheckPass)
    Button btnPass;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String phoneNum;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    String Tag = "PasswordActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.password_verification_page);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.password_page));

        if(getIntent().getExtras()!= null){
            phoneNum = getIntent().getExtras().getString("phone_num");
        }
    }

    @OnClick(R.id.btncheckPass)
    public void checkPass(){
        String passwordString = password.getText().toString();
        if(passwordString != null && !passwordString.isEmpty() &&
                passwordString.length() >1){
            String url ="api/members/checkPassword";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("phone" ,phoneNum);
            parameters.put("pass" ,passwordString);
            new sendUserDataToServer(url,parameters).execute();
//            Intent intent = new Intent(PasswordActivity.this , MainActivity.class);
//            startActivity(intent);

        }else{
            password.setError(getString(R.string.wrong_password));
        }
    }



    public class sendUserDataToServer extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;

        public sendUserDataToServer(String url, Map<String, String> parameters) {
            this.parameters = parameters;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading.setVisibility(View.VISIBLE);
            btnPass.setVisibility(View.GONE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
                Log.i(Tag,"responsecode : "+http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }

            }catch (Exception e){
            }

            if (isconnected)
                return jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            Log.i(Tag,"json: " +jsonObject);
            if(jsonObject != null && jsonObject.has("result")){
                try {
                    int result = jsonObject.getInt("result");

                if(result ==-1){
                    snackerShow(jsonObject.getString("message"));
                    Loading.setVisibility(View.GONE);
                    btnPass.setVisibility(View.VISIBLE);
                }else if(result ==1) {
                    finish();
                    Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
//                    intent.putExtra("name" ,userInfoBases.get(0).getName());
//                    intent.putExtra("family" ,userInfoBases.get(0).getFamily());
                    startActivity(intent);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Loading.setVisibility(View.GONE);
                    btnPass.setVisibility(View.VISIBLE);
                    password.setError(getString(R.string.problem_with_server_Side));
                }

            }else {
                Loading.setVisibility(View.GONE);
                btnPass.setVisibility(View.VISIBLE);
                password.setError(getString(R.string.wrong_password));

            }
        }
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

    private void snackerShow(String textMsg) {
        Snackbar snack = Snackbar.
                make(coordinatorLayout, textMsg, Snackbar.LENGTH_LONG);
        View view = snack.getView();CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

