package salavat.salavaltintorg;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.JsonParser;
import salavat.salavaltintorg.dao.UserInfoExtra;


/**
 * Created by masoomeh on 12/14/17.
 */

public class AdvUserInfoActivity extends AppCompatActivity   implements DatePickerDialog.OnDateSetListener ,
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

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

    @BindView(R.id.input_salavat_number)
    EditText salavat_num;

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

    String Tag = "AdvUserInfoActivity" ,birthdayStr;
    //    private String phoneNum = "";
    private String userId = "";
    private String dateString;
    private AppCreatorDatabase db;
    private UserInfoExtra userAdv;
    private  boolean isRegisterExtra;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language, "en"));

        setContentView(R.layout.user_info_adv);

        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
//           phoneNum = getIntent().getExtras().get("phoneNumber");
            userId = getIntent().getExtras().getString("user_id");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.intercommunity));

//        salavat_id.setItems(getResources().getStringArray(R.array.salavat));
//        salavat_id.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//
//            }
//        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvUserInfoActivity.this, CountriesLstActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        country.setHint(getString(R.string.select_your_country));

        educational.setItems(getResources().getStringArray(R.array.educational));
        educational.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedPreferences.getString(LoginActivity.language ,"en").equalsIgnoreCase("fa")) {

                    PersianCalendar persianCalendar = new PersianCalendar();
                    com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog datePickerDialog =
                            com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                            AdvUserInfoActivity.this,
                            persianCalendar.getPersianYear(),
                            persianCalendar.getPersianMonth(),
                            persianCalendar.getPersianDay()
                    );
                    datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

                }else{
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(AdvUserInfoActivity.this, AdvUserInfoActivity.this,
                            year, month, day);
                    dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    dialog.show();
                }
            }
        });

        db = Room.databaseBuilder(AdvUserInfoActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();
        fetchAdvInfo();
    }

    @OnClick(R.id.RegisterBtn)
    public void registration() {
        validation();
    }


    private void setLanguages(String lan) {

        String languageToLoad = lan; // your language
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
        Log.e(Tag, "reg: " + requestCode + " resu: " + resultCode + " data: " + data);
        if (resultCode == -1) {
            if (requestCode == 1)
                country.setText(data.getExtras().getString("country"));
        }
    }

    private void validation() {
        String occupationString, IntroducerString, cityString, nationalIdString ,salavatNumberString;
        occupationString = occupation.getText().toString();
        IntroducerString = introducer.getText().toString();
        cityString = city.getText().toString();
        nationalIdString = nationalId.getText().toString();
        salavatNumberString = salavat_num.getText().toString();


        if (!dataValidation(occupationString, occupation)) {
            return;
        } else if (!dataValidation(IntroducerString, introducer)) {
            return;
        } else if (!dataValidation(cityString, city)) {
            return;
        } else if (!dataValidation(nationalIdString, nationalId)) {
            return;
        } else if (!checkSpinnerData(educational)) {
            educational.setError(getString(R.string.error_insert_correct_edittext));
        } else if (!checkSpinnerData(country)) {
            country.setError(getString(R.string.error_insert_correct_edittext));
        } else if (!dataValidationSalavat(salavatNumberString,salavat_num)) {
            return;
        } else if (birthday.getText() == null || birthday.getText().toString().isEmpty()) {
            birthday.setError(getString(R.string.error_insert_correct_edittext));
        } else {


            String educationalStr ,countryStr ,salavatNum;
            String url = "api/members/update";
            Map<String, String> parameters = new HashMap<>();
//            parameters.put("phone",phone_number);
            parameters.put("id", userId);
            parameters.put("occupation", occupationString);
            parameters.put("introducer", IntroducerString);
            parameters.put("city", cityString);
            parameters.put("education",educationalStr = educational.getText().toString());
            parameters.put("country", countryStr = country.getText().toString());
            parameters.put("birthday",birthdayStr );
            parameters.put("groupHead", String.valueOf(chkboxHeader.isChecked()));
            parameters.put("nationalId", nationalIdString);
            parameters.put("lang",sharedPreferences.getString(LoginActivity.language, "en"));
            parameters.put("salavat_counter", salavatNum = salavat_num.getText().toString());

            userAdv = new UserInfoExtra(Integer.parseInt(userId),occupationString,birthdayStr,IntroducerString ,educationalStr
                ,countryStr ,cityString,nationalIdString,salavatNum ,chkboxHeader.isChecked());
            new sendUserDataToServer(url, parameters).execute();
        }
    }

    private boolean checkSpinnerData(MaterialSpinner spinner) {

        Log.i(Tag, "spinner: " + spinner.getText().length());
        if (spinner.getText().length() > 0)
            return true;
        else
            return false;
    }

    private boolean dataValidation(String editTextString, EditText editText) {
        Log.i(Tag, "editTextString: " + editTextString);
        if (editTextString == null || editTextString.isEmpty()) {
            editText.setError(getString(R.string.error_empty_edittext));
            return false;
        } else if (editTextString.length() < 3) {
            editText.setError(getString(R.string.less_char));
            return false;
        }
        return true;
    }


    private boolean dataValidationSalavat(String editTextString, EditText editText) {
        String regex = "\\d+";
        Log.i(Tag, "editTextString: " + editTextString);
        if (editTextString == null || editTextString.isEmpty()) {
            editText.setError(getString(R.string.error_empty_edittext));
            return false;
        } else if (!editTextString.matches(regex)) {
            editText.setError(getString(R.string.error_insert_correct_edittext));
            return false;
        }
        return true;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateString = year + "-" + month + "-" + dayOfMonth;
        birthdayStr = dateString ;
        birthday.setText(dateString);
    }

    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        dateString = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;

//        CalendarTool calendarTool = new CalendarTool();
//        calendarTool.setIranianDate(year,monthOfYear,dayOfMonth);
//        calendarTool.getGregorianDate();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int yearGregorian = cal.get(Calendar.YEAR);
//        int monthGregorian = cal.get(Calendar.MONTH);
//        int dayGregorian = cal.get(Calendar.DAY_OF_MONTH);
        birthdayStr =dateString;
//        Toast.makeText(this ,birthdayStr + "\n date: "  , Toast.LENGTH_LONG).show();
        Log.e(Tag,"convert date:  " +birthdayStr);
        birthday.setText(dateString);
    }

    public class sendUserDataToServer extends AsyncTask<Void, Void, Boolean> {
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
        protected Boolean doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
                Log.i(Tag, "responsecode : " + http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }else {
                    snackerShow(getString(R.string.internet_connection_dont_right));
                }

            } catch (Exception e) {
                snackerShow(getString(R.string.internet_connection_dont_right));
            }

            if (isconnected) {
                jsonObject = JsonParser.getJSONFromUrl(url, parameters, "POST", false);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        if (jsonObject.getString("result").equalsIgnoreCase("fail")) {
                            snackerShow(getString(R.string.try_again));
                            return false;
                        }else if (jsonObject.getString("result").equalsIgnoreCase("success")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext() , getString(R.string.messagesuccessful),Toast.LENGTH_LONG).show();
                                }
                            });
//                            snackerShow(
//                                    getString(R.string.messagesuccessful));
                            if (isRegisterExtra){
                                 db.userInfoExtraDao().updateRecord(userAdv);
                            }else {
                                long datadb = db.userInfoExtraDao().insertOnlySingleRecord(userAdv);
                                Log.e(Tag,"dataDb: " +datadb);
                            }
                            finish();
                            return  true;
                        }else
                            return false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

            } else{
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean data) {
            super.onPostExecute(data);
            Log.i(Tag, "json: " + jsonObject);
           if(!data){
                Loading.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
                snackerShow(getString(R.string.internet_connection_dont_right));
            }
        }

        private void snackerShow(String textMsg) {
            Snackbar snack = Snackbar.
                    make(coordinatorLayout, textMsg, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
        }
    }

    public void fetchAdvInfo() {
        new AsyncTask<Void, Void, List<UserInfoExtra>>() {
            @Override
            protected List<UserInfoExtra> doInBackground(Void... voids) {
                List<UserInfoExtra> userInfoExtras = db.userInfoExtraDao().fetchAllData();
                return userInfoExtras;
            }

            @Override
            protected void onPostExecute(List<UserInfoExtra> userInfoExtras) {
                if (userInfoExtras.size() == 1) {

                    isRegisterExtra = true;
                    register.setText(getString(R.string.update));
                    UserInfoExtra userInfoExtra = userInfoExtras.get(0);
                    if(sharedPreferences.getString(LoginActivity.language, "en").equalsIgnoreCase("fa")){
                        String date =  userInfoExtra.getBirthday();
                        Log.i(Tag,"dateee: "+date);
//                        String [] array = date.split("/");
//                        CalendarTool calendarTool = new CalendarTool(
//                                Integer.parseInt(array[0])
//                                ,Integer.parseInt(array[1])
//                                ,Integer.parseInt(array[2])
//                        );
                        birthday.setText(date);
                        birthdayStr = date;
                    }else{
                        birthday.setText(userInfoExtra.getBirthday());
                        birthdayStr = userInfoExtra.getBirthday();
                    }

                    country.setText(userInfoExtra.getCountry());
                    city.setText(userInfoExtra.getCity());
                    educational.setText(userInfoExtra.getEducation());
                    introducer.setText(userInfoExtra.getIntroducer());
                    salavat_num.setText(userInfoExtra.getSalavat_num());
                    occupation.setText(userInfoExtra.getOccupation());
                    nationalId.setText(userInfoExtra.getNational_id());
                    if (userInfoExtra.isGroup_head()){
                        chkboxHeader.setChecked(true);
                    }else {
                        chkboxHeader.setChecked(false);
                    }


                }
            }

        }.execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}