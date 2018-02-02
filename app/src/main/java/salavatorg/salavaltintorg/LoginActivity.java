package salavatorg.salavaltintorg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.nfc.Tag;
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
import android.widget.FrameLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavatorg.salavaltintorg.dao.JsonParser;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.login_page);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.login));

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
                        String url ="api/members";
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("phone" ,phoneNumber);
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
            next.setVisibility(View.GONE);
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
                finish();
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                intent.putExtra("phone_num",parameters.get("phoneNumber"));
                startActivity(intent);

            }else {
                Loading.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                snackerShow(getString(R.string.internet_connection_dont_right));
            }
            /**
             * that is test
             */
            Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
            intent.putExtra("phone_num",parameters.get("phoneNumber"));
            startActivity(intent);

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
}
