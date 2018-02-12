package salavatorg.salavaltintorg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavatorg.salavaltintorg.dao.JsonParser;
import salavatorg.salavaltintorg.dao.UserInfoBase;

/**
 * Created by masoomeh on 2/11/18.
 */

public class RequestForSalavatActivity extends AppCompatActivity {

    private String family,name;

    @BindView(R.id.input_name)
    EditText nameEditText;
    @BindView(R.id.btnConfirm)
    Button confirm;
    @BindView(R.id.btnCancel)
    Button cancel;
    @BindView(R.id.input_family)
    EditText familyEditText;
    @BindView(R.id.input_desc)
    EditText descEditText;
    @BindView(R.id.spinner_niat)
    MaterialSpinner spinner;
    @BindView(R.id.avloadingNiat)
    AVLoadingIndicatorView niatLoading;
    @BindView(R.id.avloadingIndicatorViewResult)
    AVLoadingIndicatorView Loading;
    String Tag = "RequestForSalavat";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_for_salavat);
        setTitle(getString(R.string.request_for_salavat));

        ButterKnife.bind(this);
        if(getIntent().getExtras()!=null){

            name = getIntent().getExtras().getString("name");
            family = getIntent().getExtras().getString("family");
            Log.e(Tag,"getIntent().getExtras(): " +name + " family: " +family);
            nameEditText.setText(name);
            familyEditText.setText(family);
        }
        new getNiatData("api/niat_category/getAll",null).execute();

    }

    @OnClick(R.id.btnConfirm)
    public void confirm(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name" ,name);
        parameters.put("family" ,family);
        parameters.put("niat" ,"shafa");
        parameters.put("desc" ,descEditText.getText().toString());
        String niat = spinner.getText().toString();
        if(niat!= null && !niat.isEmpty()) {
            new sendSalavatRequest("api/salavatrequest", parameters).execute();
        }
    }

    public class getNiatData extends AsyncTask<Void,Void,Boolean> {
        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;

        public getNiatData(String url, Map<String, String> parameters ) {
            this.parameters = parameters;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            niatLoading.setVisibility(View.VISIBLE);
            spinner.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
//                Log.i(Tag, "responsecode : " + http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }

            } catch (Exception e) {
            }

            if (isconnected) {
                jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
                Log.i(Tag, "json: " + jsonObject);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        if (jsonObject.getInt("result") == 1) {
//                            Log.i(Tag,"db: "+db);
                            JSONObject data = jsonObject.getJSONObject("data");

//                            userInfoBase.setId(data.getInt("id"));
//                            db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);
                            return  true;
                        }else if(jsonObject.getInt("result") == -1){
//                            snackerShow(jsonObject.getString("message"));
                            Toast.makeText(getApplicationContext() ,
                                    getString(R.string.messagenotsuccessful),Toast.LENGTH_LONG).show();
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
//                finish();
                spinner.setItems(getResources().getStringArray(R.array.educational));

                niatLoading.setVisibility(View.GONE);
                spinner.setEnabled(true);
//                Toast.makeText(getApplicationContext() ,
//                        getString(R.string.messagesuccessful),Toast.LENGTH_LONG).show();
            }else {
//                niatLoading.setVisibility(View.GONE);
//                spinner.setEnabled(true);
//                snackerShow(getString(R.string.internet_connection_dont_right));
                Toast.makeText(getApplicationContext() ,
                        getString(R.string.messagenotsuccessful),Toast.LENGTH_LONG).show();
            }
        }
    }




    //---------------
    public class sendSalavatRequest extends AsyncTask<Void,Void,Boolean> {
        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;

        public sendSalavatRequest(String url, Map<String, String> parameters ) {
            this.parameters = parameters;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
//                Log.i(Tag, "responsecode : " + http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }

            } catch (Exception e) {
            }

            if (isconnected) {
                jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
                Log.i(Tag, "json: " + jsonObject);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        if (jsonObject.getInt("result") == 1) {
//                            Log.i(Tag,"db: "+db);
                            JSONObject data = jsonObject.getJSONObject("data");
//                            userInfoBase.setId(data.getInt("id"));
//                            db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);
                            return  true;
                        }else if(jsonObject.getInt("result") == -1){
//                            snackerShow(jsonObject.getString("message"));
                            Toast.makeText(getApplicationContext() ,
                                    getString(R.string.messagenotsuccessful),Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext() ,
                        getString(R.string.messagesuccessful),Toast.LENGTH_LONG).show();
            }else {
                Loading.setVisibility(View.GONE);
                confirm.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
//                snackerShow(getString(R.string.internet_connection_dont_right));
            }
        }
    }
}
