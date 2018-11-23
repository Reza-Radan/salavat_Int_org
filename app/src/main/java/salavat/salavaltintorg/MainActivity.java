package salavat.salavaltintorg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.imagegallery.library.ImageGalleryFragment;
import com.etiennelawlor.imagegallery.library.activities.FullScreenImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.activities.ImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.adapters.FullScreenImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.enums.PaletteColorType;
//import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.JsonParser;
import salavat.salavaltintorg.dao.Niat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
ImageGalleryAdapter.ImageThumbnailLoader, FullScreenImageGalleryAdapter.FullScreenImageLoader {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.txtvSalavat)
    TextView txtvSalavat;

    @BindView(R.id.linearLoading)
    LinearLayout linearLoading;

    TextView userName;

    String tag = "MainActivity",error=null;
    private String name,family ,userId;

    private PaletteColorType paletteColorType;
    static SharedPreferences sharedPreferences;
    // endregion
    AppCreatorDatabase db;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_LAN, MODE_PRIVATE);
        setLanguages(sharedPreferences.getString(LoginActivity.language ,"en"));
        Log.i(tag,"lang: " +sharedPreferences.getString(LoginActivity.language ,"en"));

        setContentView(R.layout.salavat_page);

//        error.length();
        ImageGalleryActivity.setImageThumbnailLoader(this);
        ImageGalleryFragment.setImageThumbnailLoader(this);
        FullScreenImageGalleryActivity.setFullScreenImageLoader(this);

//        new pushNotificationService().CallNotification("hi" ,"bye");
        // optionally set background color using Palette for full screen images
        if(getIntent().getExtras()!=null){
            name =getIntent().getExtras().getString("name");
            family =getIntent().getExtras().getString("family");
            userId =getIntent().getExtras().getString("user_id");
        Log.i(tag,"onCreate name: " +name +" family: " +family +  " userId: " + userId);
        }

        ButterKnife.bind(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        //---
         drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width + 20;
        navigationView.setLayoutParams(params);

        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(this);
        userName =  headerLayout.findViewById(R.id.userText);
        userName.setText(name+ " " +family);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name" ,"getAllCategories");
        parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
        new getNiatData("api/niat_category",parameters).execute();

    }

    private void restartActivity(Activity activity , String lang) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LoginActivity.language,lang);
        editor.commit();

        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.finish();
            overridePendingTransition(0, 0);

            activity.startActivity(intent);
            overridePendingTransition(0, 0);
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

//    private void snackerShow(String textMsg) {
//        Snackbar snack = Snackbar.
//                make(coordinatorLayout, textMsg, Snackbar.LENGTH_LONG);
//        View view = snack.getView();CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        view.setLayoutParams(params);
//        snack.show();
//    }


    public void RequestOfSalavatClick(View v){
        Intent intent = new Intent(this , RequestForSalavatActivity.class);
        intent.putExtra("name" ,name);
        intent.putExtra("family" ,family);
        startActivity(intent);
    }

    public void ParticipateOfSalavatClick(View v){
        Intent intent = new Intent(this , AdvUserInfoActivity.class);
        intent.putExtra("user_id",userId);
        startActivity(intent);
    }

    public void TajliClick(View v){
        startActivity(new Intent(this , TajilInFarajActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.groups_menu:

//                Intent intentGRoup = new Intent(MainActivity.this, GroupListActivity.class);

//                String[] images = getResources().getStringArray(R.array.unsplash_images);
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, new ArrayList<>(Arrays.asList(images)));
//                bundle.putString(ImageGalleryActivity.KEY_TITLE, "Unsplash Images");
//                intent.putExtras(bundle);

//                startActivity(intentGRoup);
//                break;
            case R.id.archive_menu:


                String url ="api/album";
                Map<String, String> parameters = new HashMap<>();
                parameters.put("lang" ,sharedPreferences.getString(LoginActivity.language ,"en"));
                drawer.closeDrawers();
                //
                new getListOfArchive(url,parameters).execute();
            break;

        case R.id.notification_menu:
                Intent intentno = new Intent(MainActivity.this, NotificationListActivity.class);
                startActivity(intentno);
            drawer.closeDrawers();
        break;
        case R.id.lan_menu:

            Intent intent = new Intent(MainActivity.this, ChangeLangActivity.class);
            startActivityForResult(intent,1001);
            drawer.closeDrawers();
        break;
            case R.id.aboutUs_menu:

            Intent intentAboutUs = new Intent(MainActivity.this, AboutUsActivity.class);
            intentAboutUs.putExtra("url" ,"http://bonyadsalavat.com/about");
            startActivity(intentAboutUs);
                drawer.closeDrawers();
                break;

            case R.id.guide_menu:

            Intent Guide = new Intent(MainActivity.this, AboutUsActivity.class);
            Guide.putExtra("url" ,"http://bonyadsalavat.com/guide");
            startActivity(Guide);
                drawer.closeDrawers();
                break;

            case R.id.out_menu:
//                db = Room.databaseBuilder(
//                        MainActivity.this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();

                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
                break;

        }
        return false;
    }
    // region ImageGalleryAdapter.ImageThumbnailLoader Methods
    @Override
    public void loadImageThumbnail(final ImageView iv, String imageUrl, int dimension) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(dimension, dimension)
                    .centerCrop()
                    .into(iv);
        } else {
            iv.setImageDrawable(null);
        }
    }
    // endregion

    // region FullScreenImageGalleryAdapter.FullScreenImageLoader
    @Override
    public void loadFullScreenImage(final ImageView iv, String imageUrl, int width, final LinearLayout bgLinearLayout) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(width, 0)
                    .into(iv, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    applyPalette(palette, bgLinearLayout);
                                }
                            });
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            iv.setImageDrawable(null);
        }
    }
    // endregion

    // region Helper Methods
    private void applyPalette(Palette palette, ViewGroup viewGroup){
        int bgColor = getBackgroundColor(palette);
        if (bgColor != -1)
            viewGroup.setBackgroundColor(bgColor);
    }

    private void applyPalette(Palette palette, View view){
        int bgColor = getBackgroundColor(palette);
        if (bgColor != -1)
            view.setBackgroundColor(bgColor);
    }

    private int getBackgroundColor(Palette palette) {
        int bgColor = -1;

        int vibrantColor = palette.getVibrantColor(0x000000);
        int lightVibrantColor = palette.getLightVibrantColor(0x000000);
        int darkVibrantColor = palette.getDarkVibrantColor(0x000000);

        int mutedColor = palette.getMutedColor(0x000000);
        int lightMutedColor = palette.getLightMutedColor(0x000000);
        int darkMutedColor = palette.getDarkMutedColor(0x000000);

        if (paletteColorType != null) {
            switch (paletteColorType) {
                case VIBRANT:
                    if (vibrantColor != 0) { // primary option
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) { // fallback options
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    } else if (mutedColor != 0) {
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    }
                    break;
                case LIGHT_VIBRANT:
                    if (lightVibrantColor != 0) { // primary option
                        bgColor = lightVibrantColor;
                    } else if (vibrantColor != 0) { // fallback options
                        bgColor = vibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    } else if (mutedColor != 0) {
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    }
                    break;
                case DARK_VIBRANT:
                    if (darkVibrantColor != 0) { // primary option
                        bgColor = darkVibrantColor;
                    } else if (vibrantColor != 0) { // fallback options
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (mutedColor != 0) {
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    }
                    break;
                case MUTED:
                    if (mutedColor != 0) { // primary option
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) { // fallback options
                        bgColor = lightMutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    } else if (vibrantColor != 0) {
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    }
                    break;
                case LIGHT_MUTED:
                    if (lightMutedColor != 0) { // primary option
                        bgColor = lightMutedColor;
                    } else if (mutedColor != 0) { // fallback options
                        bgColor = mutedColor;
                    } else if (darkMutedColor != 0) {
                        bgColor = darkMutedColor;
                    } else if (vibrantColor != 0) {
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    }
                    break;
                case DARK_MUTED:
                    if (darkMutedColor != 0) { // primary option
                        bgColor = darkMutedColor;
                    } else if (mutedColor != 0) { // fallback options
                        bgColor = mutedColor;
                    } else if (lightMutedColor != 0) {
                        bgColor = lightMutedColor;
                    } else if (vibrantColor != 0) {
                        bgColor = vibrantColor;
                    } else if (lightVibrantColor != 0) {
                        bgColor = lightVibrantColor;
                    } else if (darkVibrantColor != 0) {
                        bgColor = darkVibrantColor;
                    }
                    break;
                default:
                    break;
            }
        }

        return bgColor;
    }


    public class getListOfArchive extends AsyncTask<Void,Void,Boolean> {


        JSONObject jsonObject;
        Map<String, String> parameters;
        String url;

        public getListOfArchive(String url, Map<String, String> parameters) {
            this.parameters = parameters;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linearLoading.setVisibility(View.VISIBLE);
//            Loading.setVisibility(View.VISIBLE);
//            next.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean isconnected = false;
            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.connect();
                Log.i(tag,"responsecode : "+http.getResponseCode());
                if (http.getResponseCode() == 200) {
                    isconnected = true;
                }else {
//                    snackerShow(getString(R.string.internet_connection_dont_right));
                }

            }catch (Exception e){

            }

            if (isconnected)
                jsonObject = JsonParser.getJSONFromUrl(url, null, "POST", false);
            if(jsonObject != null && jsonObject.has("result")){
                try {
                    if(jsonObject.has("result")) {
//                        JSONObject object = jsonObject.getJSONObject("data");
//                        Log.i(Tag,"json: " +jsonObject + " object: " + object + "  object.get(\"insert_id\").toString() " +  object.get("existence").toString());

                        if( jsonObject.getString("result").equalsIgnoreCase("success")) {
                            if (jsonObject.has("existence") && jsonObject.getString("existence").equalsIgnoreCase("1")) {
                                if (jsonObject.has("data")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    String[] images = new String[jsonArray.length()];
                                    for (int i = 0;i<jsonArray.length();i++) {
                                        JSONObject data = (JSONObject) jsonObject.getJSONArray("data").get(i);
                                        images[i] =  data.getString("image");
                                        Log.e(tag, "data: " + data.getString("image"));
                                    }


                                    Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);


                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, new ArrayList<>(Arrays.asList(images)));
                                    bundle.putString(ImageGalleryActivity.KEY_TITLE, getString(R.string.archive));
                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                    return true;
                                }
                            }
                        
                        }else
                            return false;


                    }
                } catch (JSONException e) {
                    Log.e(tag,"error: "+ e.toString());
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
//                Loading.setVisibility(View.GONE);
//                next.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,getString(R.string.internet_connection_dont_right),Toast.LENGTH_LONG).show();
//                snackerShow(getString(R.string.internet_connection_dont_right));
            }else{
                linearLoading.setVisibility(View.GONE);
            }
//                else{
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                intent.putExtra("phone_num", parameters.get("phone"));
//                //                            intent.putExtra("userId", object.get("insert_id").toString());
//                startActivity(intent);
//            }

        }
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
//            niatLoading.setVisibility(View.VISIBLE);
//            spinner.setEnabled(false);
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
                Log.i(tag, "json: " + jsonObject);
                if (jsonObject != null && jsonObject.has("result")) {
                    try {
                        if (jsonObject.getString("result").equalsIgnoreCase("success")) {
//                            Log.i(Tag,"db: "+db);
//                            JSONArray data = jsonObject.getJSONArray("data");
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        txtvSalavat.append(" " + jsonObject.getString("salavatCount"));
                                    } catch (JSONException e) {
                                        txtvSalavat.append(" - ");
                                    }
                                }
                            });

                            return  true;
                        }else if(jsonObject.getString("result").equalsIgnoreCase("fail")){
//                            snackerShow(jsonObject.getString("message"));

                            runOnUiThread(new Runnable() {
                                public void run() {
                                Toast.makeText(getApplicationContext() ,
                                        getString(R.string.messagenotsuccessful),Toast.LENGTH_LONG).show();
                                }
                            });
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

            Log.i(tag,"json: " +jsonObject);
            if(hasdata){

            }else {
                Toast.makeText(getApplicationContext() ,
                        getString(R.string.try_again),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(tag,"requestCode: " +requestCode + " resultCode: " +resultCode
        + " data: " +data);

        if(requestCode ==1001 && resultCode == 1001 && data !=null) {
            String key = data.getStringExtra("key");
            Log.i(tag, "requestCode: " + requestCode + " resultCode: " + resultCode
                    + " data: " + " key: " + key);
            if (resultCode == 1001 && key != null) {
                if (key.equalsIgnoreCase("fa")) {
                    restartActivity(MainActivity.this, "fa");
                } else if (key.equalsIgnoreCase("en")) {
                    restartActivity(MainActivity.this, "en");
                }
            }

        }
    }

}
