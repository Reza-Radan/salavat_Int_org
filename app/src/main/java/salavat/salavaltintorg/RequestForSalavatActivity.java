package salavat.salavaltintorg;

import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavat.salavaltintorg.dao.JsonParser;
import salavat.salavaltintorg.dao.Niat;

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
    private String niatIdDelected;
    SharedPreferences sharedPreferences;

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

         sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name" ,"getAllCategories");
        parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
        new getNiatData("api/niat_category",parameters).execute();

    }

    @OnClick(R.id.btnConfirm)
    public void confirm(){
        Map<String, String> parameters = new HashMap<>();
        name = nameEditText.getText().toString();
        family =  familyEditText.getText().toString();
        if(!name.isEmpty() && !family.isEmpty() && name.length()>=3 && family.length() >=3) {
            parameters.put("name", name);
            parameters.put("family", family);
            parameters.put("niat_category_id", niatIdDelected);
            parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
            parameters.put("description", descEditText.getText().toString());
            String niat = spinner.getText().toString();
            if (niat != null && !niat.isEmpty()) {
                new sendSalavatRequest("api/salavatrequest", parameters).execute();
            }
        }else if (name.isEmpty()){
            nameEditText.setError(getString(R.string.error_empty_edittext));
        }else if(family.isEmpty()){
            familyEditText.setError(getString(R.string.error_empty_edittext));
        }else if (!(name.length()>=3)){
            nameEditText.setError(getString(R.string.less_char_name));
        }else if(!(family.length()>=3)){
            familyEditText.setError(getString(R.string.less_char_family));
        }
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        finish();
    }


    public class getNiatData extends AsyncTask<Void,Void,Boolean> {
        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;
        List<Niat> niat = new ArrayList<>();

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
                        if (jsonObject.getString("result").equalsIgnoreCase("success")) {
//                            Log.i(Tag,"db: "+db);
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i <data.length() ; i++) {
//                                Log.w(Tag,"json: " + ((JSONObject)data.get(i)).getString("text"));
                                niat.add(new Niat(Integer.parseInt(((JSONObject)data.get(i)).getString("id")),((JSONObject)data.get(i)).getString("text")));
                            }

//                            db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);
//                            userInfoBase.setId(data.getInt("id"));
                            return  true;
                        }else if(jsonObject.getString("result").equalsIgnoreCase("fail")){
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
                niatLoading.setVisibility(View.INVISIBLE);
                spinner.setEnabled(true);
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i <niat.size(); i++) {
                    arrayList.add(niat.get(i).getDes_fa());
                }
                spinner.setItems(arrayList);
                niatIdDelected = String.valueOf(niat.get(0).getId());
                spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                        niatIdDelected = view.getItems().get(position).toString();
                        String niatSelected = view.getItems().get(position).toString();
                        for (int i = 0; i <niat.size() ; i++) {
                          if (niatSelected.contains(niat.get(i).getDes_fa())){
//                            Log.i(Tag,"niatIdDelected: " +);
                              niatIdDelected =  String.valueOf(niat.get(i).getId());
                          }
                        }
                    }
                });
            }else {
                niatLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext() ,
                        getString(R.string.try_again),Toast.LENGTH_LONG).show();
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
                        if (jsonObject.getString("result").equalsIgnoreCase("success")) {
//                            Log.i(Tag,"db: "+db);
                            JSONObject data = jsonObject.getJSONObject("data");
//                            userInfoBase.setId(data.getInt("id"));
//                            db.userInfoBaseDao().insertOnlySingleRecord(userInfoBase);
                            return  true;
                        }else if(jsonObject.getString("result").equalsIgnoreCase("fail")){
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
                Loading.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
//                snackerShow(getString(R.string.internet_connection_dont_right));
            }
        }
    }
}
