package salavat.salavaltintorg;

import android.arch.persistence.room.Room;
import android.content.Context;
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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.JsonParser;
import salavat.salavaltintorg.dao.UserInfoBase;
import salavat.salavaltintorg.dao.UserInfoExtra;

/**
 * Created by masoomeh on 12/14/17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.spinner_prePhone)
    MaterialSpinner prePhoneSpinner;

    @BindView(R.id.PhoneNumber)
    EditText phoneNumberetxt;

    @BindView(R.id.spinnerSelectLan)
    MaterialSpinner selectionLanSpinner;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.avloadingIndicatorViewResult)
    AVLoadingIndicatorView Loading;

    @BindView(R.id.Next)
    Button next;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String Tag = "LoginActivity";
    static String SHARED_LAN = "sharePrefLan";
    static String language ="lan";
    List<String> prePhone = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private AppCreatorDatabase db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.login_page);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.login));

        db = Room.databaseBuilder(LoginActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();
        deleteAllUSers();

        String GetCountryZipCode = GetCountryZipCode();
        if(GetCountryZipCode != null && !GetCountryZipCode.isEmpty()){
            String[] androidStrings = getResources().getStringArray(R.array.CountryCodes);
            for (String s : androidStrings) {
                int i = s.indexOf(GetCountryZipCode);
                if (i >= 0) {
                    prePhone.add(0,s);
                }else{
                    prePhone.add(s);
                }
                prePhoneSpinner.setItems(prePhone);
            }
        }
        else
            prePhoneSpinner.setItems(getResources().getStringArray(R.array.CountryCodes));

        selectionLanSpinner.setItems(getResources().getStringArray(R.array.languages));
        selectionLanSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                if(item.equalsIgnoreCase("فارسی")) {
                    setLanguages("fa");
                    getSupportActionBar().setTitle(getString(R.string.login));
                    next.setText(getString(R.string.next));
                }else if(item.equalsIgnoreCase("english")) {
                    setLanguages("en");
                    getSupportActionBar().setTitle(getString(R.string.login));
                    next.setText(getString(R.string.next));
                }

            }
        });
    }

    @OnClick(R.id.Next)
    public void next(){

        String prePhone, selectionLan , phoneNumber;
        prePhone     = prePhoneSpinner.getText().toString();
        selectionLan = selectionLanSpinner.getText().toString();
        phoneNumber  = phoneNumberetxt.getText().toString();
        if(prePhone != null && !prePhone.isEmpty() && prePhone.length() >0){

            if(selectionLan != null && !selectionLan.isEmpty() && selectionLan.length() >0){

                if(phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.length() >0 ){
                    if(phoneNumberValidate(phoneNumber)){
                        //send number to server and return data
                        String url ="api/members/getMemberInfo/";
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("phone" ,"+"+prePhone.substring(0,prePhone.indexOf(","))+phoneNumber);
                        SharedPreferences sharedPreferencesgoogle = getSharedPreferences(Application.googleId, MODE_PRIVATE);
                        parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
                        ///
                        String google_id = sharedPreferencesgoogle.getString(Application.token, "-1");
                        Log.e(Tag,"regID:  "+google_id);
                        parameters.put("push_id" ,google_id);
                        //
                        new sendUserDataToServer(url,parameters).execute();

                    }else{
                        phoneNumberetxt.setError(getString(R.string.error_insert_correct_your_phone));
//                        snackerShow(getString(R.string.insert_your_phone));

                    }

                }else{
                    phoneNumberetxt.setError(getString(R.string.insert_your_phone));
                    Snackbar.
                            make(coordinatorLayout, getString(R.string.insert_your_phone), Snackbar.LENGTH_LONG).show();

                }

            }else{
                selectionLanSpinner.setError(getString(R.string.select_lan));
            }

        }else{
            prePhoneSpinner.setError(getString(R.string.select_prePhone));
            Snackbar.
                    make(coordinatorLayout, getString(R.string.select_prePhone), Snackbar.LENGTH_LONG).show();
        }

    }



    /**
     * should check data phone nuber is correct
     * @param phoneNumber
     * @return
     */

    public Boolean phoneNumberValidate(String phoneNumber)
    {
        if (phoneNumber.matches("\\d{10}")&&phoneNumber.startsWith("9"))
        {
            return true;
        }else{
            return false;
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
            next.setVisibility(View.GONE);
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
                }

            }catch (Exception e){

                }

            if (isconnected)
                jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
            if(jsonObject != null && jsonObject.has("result")){
                try {
                    if(jsonObject.has("result")) {
//                        JSONObject object = jsonObject.getJSONObject("data");
//                        Log.i(Tag,"json: " +jsonObject + " object: " + object + "  object.get(\"insert_id\").toString() " +  object.get("existence").toString());

                        if( jsonObject.getString("result").equalsIgnoreCase("success"))
                            if(  jsonObject.has("existence") &&  jsonObject.getString("existence").equalsIgnoreCase("0")){
                                finish();
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                intent.putExtra("phone_num", parameters.get("phone"));
                                startActivity(intent);
                                return true;
                            }else if (jsonObject.has("existence") &&  jsonObject.getString("existence").equalsIgnoreCase("1")){

                                if (jsonObject.has("data")){
                                    JSONObject data = (JSONObject) jsonObject.getJSONArray("data").get(0);
                                    Log.e(Tag,"data: " +data);

                                    UserInfoBase userInfoBase = new UserInfoBase();
                                    userInfoBase.setFamily(data.getString("family"));
                                    userInfoBase.setName(data.getString("name"));
                                    userInfoBase.setId( Integer.parseInt(data.getString("id")));
                                    userInfoBase.setPhone_num(parameters.get("phone"));
                                    userInfoBase.setGender(data.getString("gender"));
                                    userInfoBase.setEmail(data.getString("email"));
                                    long datadb = db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);
                                    if (datadb == -1) {
                                        db.userInfoBaseDao().updateRecord(userInfoBase);
                                    }
                                    Log.e(Tag,"dataDb: " +datadb + " has: " +data.has("occupation"));

                                    if(data.has("occupation") && !data.getString("occupation").isEmpty() && !data.getString("occupation").equalsIgnoreCase("")){
//                                        data: {"id":"28","birthday":"2018\/41\/3","flag":"android","salavat_counter":"0","country":"Iran","city":"تهران","":"","":"فوق دیپلم","groupHead":"fals","introducer":"معصومه عبدوس","lang":"fa","group_id":"","registerDate":"2018-06-06 02:52:21","last_login":"0000-00-00 00:00:00"}
                                        UserInfoExtra userInfoExtra = new UserInfoExtra((int)datadb,
                                                data.getString("occupation"),
                                                data.getString("birthday"),
                                                data.getString("introducer"),
                                                data.getString("education"),
                                                data.getString("country"),
                                                data.getString("city"),
                                                data.getString("nationalId"),
                                                data.getString("salavat_counter"),
                                                data.getBoolean("groupHead"));

                                        long datadbinsert = db.userInfoExtraDao().insertOnlySingleRecord(userInfoExtra);
                                        if (datadbinsert == -1) {
                                            db.userInfoExtraDao().updateRecord(userInfoExtra);
                                        }


                                    }
//                                    long datadb = db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);


                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, PasswordActivity.class);
                                    intent.putExtra("phone_num", parameters.get("phone"));
                                    intent.putExtra("name", data.getString("name"));
                                    intent.putExtra("family", data.getString("family"));
                                    startActivity(intent);
                                    return true;
                                }else
                                    return false;

                            }else {
                                return false;
                            }

                    }
                } catch (JSONException e) {
                    Log.e(Tag,"error: "+ e.toString());
                    e.printStackTrace();
                   return false;

                }
            }else {
                return false;
            }

            return false;
           /* else
                return false;*/
        }

        @Override
        protected void onPostExecute(Boolean data) {
            super.onPostExecute(data);
            if (!data){
                Loading.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                snackerShow(getString(R.string.internet_connection_dont_right));
                }
//                else{
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                intent.putExtra("phone_num", parameters.get("phone"));
//                //                            intent.putExtra("userId", object.get("insert_id").toString());
//                startActivity(intent);
//            }

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

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_LAN, MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString(language,lan);
        editor.commit();

    }


    private String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
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

    public void deleteAllUSers() {
        new AsyncTask<Void, Void, List<UserInfoBase>>() {
            @Override
            protected List<UserInfoBase> doInBackground(Void... voids) {
                db.userInfoBaseDao().deleteAllRecords();
                db.userInfoExtraDao().deleteAllRecords();
//                db.userInfoExtraDao().deleteAllRecords();
//                db.userInfoExtraDao().deleteAllRecords();
//                db.userInfoExtraDao().deleteAllRecords();

//                Log.i(Tag,"data: " +data);
                return null;
            }

            @Override
            protected void onPostExecute(List<UserInfoBase> userInfoBases) {}
        }.execute();
    }

}
