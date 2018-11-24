package salavat.salavaltintorg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by masoomeh on 2/11/18.
 */

public class ChangeLangActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    MaterialSpinner spinner;


    String Tag ="TajilInFarajActivity";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language, "en"));
        setContentView(R.layout.select_lang);
        ButterKnife.bind(this);
        setTitle(getString(R.string.Select_language));

        Log.e(Tag,"English: " +sharedPreferences.getString(LoginActivity.language, "en"));
        if (sharedPreferences.getString(LoginActivity.language, "en").equalsIgnoreCase("en")){
            spinner.setItems(getResources().getStringArray(R.array.language));
    }else  if (sharedPreferences.getString(LoginActivity.language, "en").equalsIgnoreCase("fa")) {
            spinner.setItems(getResources().getStringArray(R.array.language_fa));
    }

//        Log.e(Tag ,"111 spinner.getItems() spinner: "+ spinner.getText());

//        spinner.setGravity(Gravity.CENTER |Gravity.FILL);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Log.e(Tag,"English: " +item);
                if(item.equalsIgnoreCase("فارسی")) {
                    Intent intent = new Intent();
                    intent.putExtra("key", "fa");
                    setResult(1001,intent);
                    finish();
                }else if(item.equalsIgnoreCase("english")) {
                    Log.e(Tag,"11English: " +item);
                    Intent intent = new Intent();
                    intent.putExtra("key", "en");
                    setResult(1001,intent);
                    finish();
                }
            }
        });

    }




    private void setLanguages(String lan){

        Log.i(Tag,"lang: " +lan);
        String languageToLoad  = lan; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }



}
