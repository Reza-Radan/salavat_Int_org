package salavatorg.salavaltintorg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.user_info_adv);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.intercommunity));

        salavat_id.setItems(getResources().getStringArray(R.array.salavat));
        salavat_id.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });

        country.setItems(getResources().getStringArray(R.array.countries_array));
        country.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });


        educational.setItems(getResources().getStringArray(R.array.educational));
        educational.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });


    }

    @OnClick(R.id.RegisterBtn)
    public void registration(){

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
