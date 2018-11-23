package salavat.salavaltintorg;

import android.arch.persistence.room.Room;
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
import android.widget.TextView;

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
import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.JsonParser;

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
    private String phoneNum ,name ,family;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.txtvPhpneNumPass)
    TextView phoneNumTextView;

    @BindView(R.id.passwordMsg)
    TextView txtvSendSms;
    String Tag = "PasswordActivity", passwordString ;
    private AppCreatorDatabase db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.password_verification_page);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.password_page));

        if(getIntent().getExtras()!= null){
            phoneNum = getIntent().getExtras().getString("phone_num");
            name = getIntent().getExtras().getString("name");
            family = getIntent().getExtras().getString("family");
            phoneNumTextView.setText(phoneNum);
        }
    }

    @OnClick(R.id.btncheckPass)
    public void checkPass(){
         passwordString = password.getText().toString();
        if(passwordString != null && !passwordString.isEmpty() &&
                passwordString.length() >1){
            String url ="api/members/checkUserPass";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("phone" ,phoneNum);
            parameters.put("password" ,passwordString);
            parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
            db = Room.databaseBuilder(PasswordActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();
            new sendUserDataToServer(url,parameters).execute();
//            Intent intent = new Intent(PasswordActivity.this , MainActivity.class);
//            startActivity(intent);

        }else{
            password.setError(getString(R.string.wrong_password));
        }
    }



    public class sendUserDataToServer extends AsyncTask<Void,Void,Boolean> {
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
        protected Boolean doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
                Log.i(Tag,"responsecode : "+http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }else{
                    snackerShow(getString(R.string.internet_connection_dont_right));
                }

            }catch (Exception e){
                snackerShow(getString(R.string.internet_connection_dont_right));
            }

            if (isconnected) {
                jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
                Log.i(Tag, "json: " + jsonObject);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        String result = jsonObject.getString("result");
                        if (result.equalsIgnoreCase("success")) {
                            JSONObject data = (JSONObject) jsonObject.getJSONArray("data").get(0);
                            finish();
                            Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                            intent.putExtra("name" ,name);
                            intent.putExtra("family" ,family);
                            intent.putExtra("user_id" ,data.getString("id"));
//                            UserInfoBase userInfoBase = new UserInfoBase();
//                            userInfoBase.setPassword(passwordString);
//                            userInfoBase.setId(Integer.parseInt(data.getString("id")));
                            int dataa = db.userInfoBaseDao().updatePassword(Integer.parseInt(data.getString("id")) ,passwordString);
                            Log.e(Tag,"dataaa: " +dataa);
                            startActivity(intent);
                            return true;

                        } else if (result.equalsIgnoreCase("fail")) {
                            snackerShow(jsonObject.getString("message"));
//                            password.setError(getString(R.string.problem_with_server_Side));
                            return false;
                        } else {
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                }else
                    return false;
            }
            else
                return false;
        }


        @Override
        protected void onPostExecute(Boolean data) {
            super.onPostExecute(data);

                if(data){

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
        startActivity(new Intent(PasswordActivity.this ,LoginActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSupportNavigateUp();
    }


    public void sendPassword(View view){
        String url ="api/members/resendPassword";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("phone" ,phoneNum);
        parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
        new sendSms(url ,parameters).execute();
    }

    private class sendSms extends AsyncTask<Void, Void, Boolean> {
        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;

        public sendSms(String url, Map<String, String> parameters) {
            this.parameters = parameters;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtvSendSms.setTextColor(getResources().getColor(R.color.gray_blue));
            txtvSendSms.setClickable(false);
        }

        @Override
        protected Boolean doInBackground(Void... params){

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
                Log.i(Tag, "responsecode : " + http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }else{
                    snackerShow(getString(R.string.internet_connection_dont_right));
                }
            }catch(Exception e) {
                snackerShow(getString(R.string.internet_connection_dont_right));
            }

            if(isconnected){
                jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
                Log.i(Tag, "json: " + jsonObject);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        String result = jsonObject.getString("result");
                        if (result.equalsIgnoreCase("success")) {
//                            JSONObject data = (JSONObject) jsonObject.getJSONArray("data").get(0);
//                            snackerShow(
//                                    getString(R.string.messagesuccessful));
                            return true;

                        } else if (result.equalsIgnoreCase("fail")) {
                            snackerShow(jsonObject.getString("message"));
    //                            password.setError(getString(R.string.problem_with_server_Side));
                            return false;
                        } else {
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                } else
                    return false;
            }
                else
                        return false;
    }

        @Override
        protected void onPostExecute(Boolean data) {
            txtvSendSms.setTextColor(getResources().getColor(R.color.blue_500));
            txtvSendSms.setClickable(true);
            super.onPostExecute(data);

        }
    }
}

