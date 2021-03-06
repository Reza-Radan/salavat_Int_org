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
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.JsonParser;
import salavat.salavaltintorg.dao.UserInfoBase;

/**
 * Created by masoomeh on 12/14/17.
 */

public class RegisterActivity extends AppCompatActivity{
    @BindView(R.id.txtvPhpneNum)
    TextView phoneNumber;
    @BindView(R.id.input_name)
    EditText name;
    @BindView(R.id.input_family)
    EditText family;
    @BindView(R.id.input_email)
    EditText email;
    @BindView(R.id.male)RadioButton rbfemale;
    @BindView(R.id.fmale)RadioButton rbmale;
    @BindView(R.id.radioGroup)RadioGroup  radioGroup;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.RegisterBtn)
    Button registerBtn;
    @BindView(R.id.avloadingIndicatorViewResult)
    AVLoadingIndicatorView Loading;
    String phoneNum ,userId, Tag= "RegisterActivity";
    private AppCreatorDatabase db;
    private String google_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.register_page);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.register));

        if(getIntent().getExtras()!= null){
            phoneNumber.setText(phoneNum = getIntent().getExtras().getString("phone_num"));
            userId = getIntent().getExtras().getString("userId");
        }
        db = Room.databaseBuilder(RegisterActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();

        if(userId != null && !userId.equalsIgnoreCase("") && !userId.isEmpty()){
            getDataUserById(userId);
        }
    }

    private void validation() {
        String nameString , familyString , emailString ,genderString = "-1";
        nameString = name.getText().toString();
        familyString = family.getText().toString();
        emailString = email.getText().toString();

        if(nameString.length()<=2 || nameString.matches("[0-9_-]")){
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//            snackbar.show();
            name.setError(getString(R.string.less_char_name));

        }else if(familyString.length()<=3 || familyString.matches("[0-9_-]")){
            family.setError(getString(R.string.less_char_family));
        }else if(!emailValidation(emailString)){
            email.setError(getString(R.string.email_validation));
        }else if(!rbfemale.isChecked() && !rbmale.isChecked()){

            Snackbar.
                    make(coordinatorLayout, getString(R.string.select_gender), Snackbar.LENGTH_LONG).show();
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(Application.googleId ,MODE_PRIVATE);

             if(rbfemale.isChecked()){
                genderString = "0";
            }
            else if(rbmale.isChecked()){
                genderString = "1";
            }
            google_id = sharedPreferences.getString(Application.token,"-1");
            Log.e(Tag,"regID:  "+google_id);
            String url ="api/members/create";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("phone" ,phoneNum);
            parameters.put("name" ,nameString);
            parameters.put("family" ,familyString);
            parameters.put("email" ,emailString);
            parameters.put("gender" ,genderString);
            parameters.put("push_id" ,google_id);
            parameters.put("flag" ,"android");
            parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));

            UserInfoBase userInfoBase = new UserInfoBase("-1",phoneNum,nameString,familyString,
                    emailString,genderString,google_id);

            Log.i(Tag,"phoneNum: " +genderString);


            new sendUserDataToServer(url,parameters ,userInfoBase).execute();
        }
    }


    private boolean emailValidation(String emailString) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(emailString).matches();
    }


    @OnClick(R.id.RegisterBtn)
    public void registerPage(){
        validation();
    }


    public class sendUserDataToServer extends AsyncTask<Void,Void,Boolean> {
        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;
        UserInfoBase userInfoBase;

        public sendUserDataToServer(String url, Map<String, String> parameters , UserInfoBase userInfoBase) {
            this.parameters = parameters;
            this.url = url;
             this.userInfoBase = userInfoBase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

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

            } catch (Exception e) {
                snackerShow(getString(R.string.internet_connection_dont_right));
            }

            if (isconnected) {
                 jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
                Log.i(Tag, "json: " + jsonObject);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        if (jsonObject.getString("result").equalsIgnoreCase("success")) {
                            Log.i(Tag,"db: "+db);
                            snackerShow(
                                    getString(R.string.messagesuccessful));
                            JSONObject data = jsonObject.getJSONObject("data");
                            userInfoBase.setId(data.getInt("id"));
                           long datadb =   db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);
                            Log.e(Tag,"dataDb: " +datadb);
                            if(datadb ==-1){
                                db.userInfoBaseDao().updateRecord(userInfoBase);
                            }
                            return  true;
                        }else if(jsonObject.getString("result").equalsIgnoreCase("fail")){
                            snackerShow(jsonObject.getString("message"));
                            return false;
                        }else
                            return false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                }else
                    return false;
            }else
                return false;

        }

        @Override
        protected void onPostExecute(Boolean hasdata) {

            Log.i(Tag,"json: " +jsonObject);
            if(hasdata){
                finish();
                Intent intent = new Intent(RegisterActivity.this, PasswordActivity.class);
                intent.putExtra("phone_num",phoneNum);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("user_id", String.valueOf(userId));
                intent.putExtra("family",family.getText().toString());
                startActivity(intent);

            }else {
                Loading.setVisibility(View.GONE);
                registerBtn.setVisibility(View.VISIBLE);
//                snackerShow(getString(R.string.internet_connection_dont_right));
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
        startActivity(new Intent(this ,LoginActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSupportNavigateUp();
    }
    public void getDataUserById(String userId) {

        final int userIdInt;
        try{
            userIdInt = Integer.parseInt(userId);
        }catch (Exception e){
            snackerShow(getString(R.string.try_again));
            return;
        }
        new AsyncTask<Void, Void, UserInfoBase>() {
            @Override
            protected UserInfoBase doInBackground(Void... voids) {
                UserInfoBase userInfoBases = db.userInfoBaseDao().getSingleRecord(userIdInt);
                return userInfoBases;
            }

            @Override
            protected void onPostExecute(UserInfoBase userInfoBases) {
                super.onPostExecute(userInfoBases);
//                Log.e(Tag,"MainActivity:size: " + userInfoBases.size());
//                Log.e("data" ,"number: "+"phone_num"+ userInfoBases.getName());
                //  this.name = nameString;
                //        this.family = familyString;
                //        this.gender = genderString;
                //        this.google_id = google_id;
                //        this.email = emailString;

                //  TextView phoneNumber;
                //    @BindView(R.id.input_name)
                //    EditText name;
                //    @BindView(R.id.input_family)
                //    EditText family;
                //    @BindView(R.id.input_email)
                //    EditText email;
                if (userInfoBases != null) {
                    Log.e("data", "number: " + "phone_num" + userInfoBases.getPhone_num()
                            + "name: " +userInfoBases.getGender());
                    phoneNumber.setText( userInfoBases.getPhone_num());
                    name.setText(userInfoBases.getName());
                    family.setText(userInfoBases.getFamily());
                    email.setText(userInfoBases.getEmail());
                    if (userInfoBases.getGender().equalsIgnoreCase("0")){
                        Log.e("data", "number: " + "phone_num" + userInfoBases.getPhone_num()
                                + "name: " +userInfoBases.getGender() + "  in if");
                        rbfemale.setChecked(true);
                    }else if (userInfoBases.getGender().equalsIgnoreCase("1")){
                        rbmale.setChecked(true);
                    }

                }

            }

        }.execute();
    }
}
