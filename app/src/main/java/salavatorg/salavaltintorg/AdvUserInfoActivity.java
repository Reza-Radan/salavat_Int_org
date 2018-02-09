package salavatorg.salavaltintorg;

import android.app.Activity;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

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

import static salavatorg.salavaltintorg.R.id.coordinatorLayout;

/**
 * Created by masoomeh on 12/14/17.
 */

public class AdvUserInfoActivity extends AppCompatActivity {

    @BindView(R.id.input_occupation)
    EditText occupation;

    @BindView(R.id.input_introducer)
    EditText introducer;

    @BindView(R.id.spinnerEducational)
    MaterialSpinner educational;

    @BindView(R.id.spinnerCountry)
    MaterialSpinner country;

    @BindView(R.id.input_city)
    EditText city;

    @BindView(R.id.input_national_id)
    EditText nationalId;

    @BindView(R.id.spinnersalavatNum)
    MaterialSpinner salavat_id;

    @BindView(R.id.chkbxGroupHead)
    CheckBox chkboxHeader;

    @BindView(R.id.Birthday)
    Button birthday;

    @BindView(R.id.RegisterBtn)
    Button register;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.avloadingIndicatorViewResult)
    AVLoadingIndicatorView Loading;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    String Tag= "AdvUserInfoActivity";
    private String phoneNum = "";
    private String userId= "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.user_info_adv);

        ButterKnife.bind(this);

        if (getIntent().getExtras() != null){
            getIntent().getExtras().get("phoneNumber");
            getIntent().getExtras().get("userId");
            
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.intercommunity));

        salavat_id.setItems(getResources().getStringArray(R.array.salavat));
        salavat_id.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvUserInfoActivity.this , CountriesLstActivity.class);
                startActivityForResult(intent,1);
            }
        });
        country.setHint(getString(R.string.select_your_country));

        educational.setItems(getResources().getStringArray(R.array.educational));
        educational.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });


    }

    @OnClick(R.id.RegisterBtn)
    public void registration(){
        validation();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(Tag,"reg: "+requestCode +" resu: " +resultCode + " data: " +data);
        if(resultCode ==-1){
            if(requestCode ==1)
                country.setText(data.getExtras().getString("country"));
        }
    }

    private void validation() {
        String occupationString , IntroducerString , cityString,nationalIdString;
        occupationString = occupation.getText().toString();
        IntroducerString = introducer.getText().toString();
        cityString = city.getText().toString();
        nationalIdString = nationalId.getText().toString();


        if(!dataValidation(occupationString ,occupation)){
            return;
        }else if(!dataValidation(IntroducerString ,introducer)){
            return;
        }else if(!dataValidation(cityString ,city)){
            return;
        }else if(!dataValidation(nationalIdString ,nationalId)){
            return;
        }else if (!checkSpinnerData(educational)){
            educational.setError(getString(R.string.error_insert_correct_edittext));
        }else if (!checkSpinnerData(country)){
            country.setError(getString(R.string.error_insert_correct_edittext));
        }else if (!checkSpinnerData(salavat_id)){
            salavat_id.setError(getString(R.string.error_insert_correct_edittext));
        }else{
            String url ="http://www.feel-fresh.com/getCompanyItem.php";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("phoneNumber" ,phoneNum);
            parameters.put("userID" ,userId);
            new sendUserDataToServer(url,parameters).execute();
        }
    }

    private boolean checkSpinnerData(MaterialSpinner spinner) {

        Log.i(Tag,"spinner: " +spinner.getText().length());
        if (spinner.getText().length()>0)
            return true;
        else
            return false;
    }

    private boolean dataValidation(String editTextString, EditText editText) {
        Log.i(Tag,"editTextString: " +editTextString);
        if (editTextString == null || editTextString.isEmpty()) {
            editText.setError(getString(R.string.error_empty_edittext));
            return false;
        }
        else if (editTextString.length() <3) {
            editText.setError(getString(R.string.less_char));
            return false;
        }
        return true;
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
            register.setVisibility(View.GONE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL(this.url);
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
//                Intent intent = new Intent(AdvUserInfoActivity.this , PasswordActivity.class);
//                startActivity(intent);

            }else {
                Loading.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
                snackerShow(getString(R.string.internet_connection_dont_right));
            }
            /**
             * that is test
             */
//            Intent intent = new Intent(RegisterActivity.this , PasswordActivity.class);
//            intent.putExtra("phone_num",phoneNum);
//            startActivity(intent);

        }
    }
    private void snackerShow(String textMsg) {
        Snackbar snack = Snackbar.
                make(coordinatorLayout, textMsg, Snackbar.LENGTH_LONG);
        View view = snack.getView();CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snack.show();
    }
}
