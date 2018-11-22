package salavat.salavaltintorg;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import salavat.salavaltintorg.dao.JsonParser;

/**
 * Created by masoomeh on 2/11/18.
 */

public class TajilInFarajActivity extends AppCompatActivity {

    @BindView(R.id.avloadingIndicatorViewResultTa)
    AVLoadingIndicatorView Loading;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.input_salavat_number)
    EditText counter;

    String Tag ="TajilInFarajActivity";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));
        setContentView(R.layout.tajil_in_faraj_page);
        ButterKnife.bind(this);
        setTitle(getString(R.string.hurry_faraj));
    }


    @OnClick(R.id.btnConfirm)
    public void confirm(){
        String counterStr = counter.getText().toString();
        Map<String, String> parameters = new HashMap<>();

        if (counterStr.isEmpty()){
            counter.setError(getString(R.string.error_empty_edittext));
        }else if (!isNumeric(counterStr)){
            counter.setError(getString(R.string.error_insert_correct_edittext));
        }else {
            parameters.put("counter" ,counterStr);
            parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
            new sendUserDataToServer("api/tajil", parameters).execute();
        }
    }

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        finish();
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
            btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
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

            if(jsonObject != null && jsonObject.has("result")){
                try {
                    if(jsonObject.getString("result").equalsIgnoreCase("success")) {
//                        JSONObject object = jsonObject.getJSONObject("data");
//                        Log.i(Tag,"json: " +jsonObject + " object: " + object + "  object.get(\"insert_id\").toString() " +  object.get("insert_id").toString());

                        finish();
//                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                        intent.putExtra("phone_num", parameters.get("phone"));
//                        intent.putExtra("userId", object.get("insert_id").toString());
//                        startActivity(intent);
                    }else if (jsonObject.getString("result").equalsIgnoreCase("fail")){
                        Toast.makeText(TajilInFarajActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Loading.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
//                    snackerShow(getString(R.string.internet_connection_dont_right));

                }
            }else {
                Loading.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
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

}
