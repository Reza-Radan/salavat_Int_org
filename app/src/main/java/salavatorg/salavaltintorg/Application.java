package salavatorg.salavaltintorg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import java.io.IOException;

/**
 * Created by masoomeh on 4/27/18.
 */

public class Application extends android.app.Application {

    Context context;
    static GoogleCloudMessaging gcm ;
    static String RegId="",Tag = "Application";
    String PROJECT_NUMBER = "964828230076";
    public static String googleId = "GOOGLEID";
    public static String token = "token";
    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        context = getApplicationContext();
       getGoogleId();
    }

    /**
     * get Google Registration ID from Google
     */
    private void getGoogleId() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    RegId= gcm.register(PROJECT_NUMBER)+"@gmail.com" ;

                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                    SharedPreferences sharedPreferences = getSharedPreferences(googleId,MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(token,RegId);
                    edit.commit();
                    Log.d(Tag, "Refreshed token: " + refreshedToken +"\n regID: "+ RegId
                    + " \n share: " +sharedPreferences.getString(token,"-1"));

//                        editor.putString("RegistrationID",RegId);
//                        editor.commit();


                } catch (IOException ex) {
                    Log.e(Tag,"error: registerId: " +ex);
                }

                Log.e(Tag,"registerId: " +RegId);
                return RegId;
            }
            @Override
            protected void onPostExecute(String msg) {

                OneSignal.setEmail(RegId);
                OneSignal.startInit(getApplicationContext())

//                        .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
//                            @Override
//                            public void notificationReceived(OSNotification notification) {
//                                Log.i(tag,"notification: " +notification.androidNotificationId );
//                            }
//                        })
//                        .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
//                            @Override
//                            public void notificationOpened(OSNotificationOpenResult result) {
//                                JSONObject jsonObject = result.toJSONObject();
//                                Log.v(tag,"notification: result: " +result.toJSONObject() );
////                                Intent intent = new Intent(getApplicationContext() , viewCompany.class);
////                                intent.putExtra("company_id" , "21129");
////                                startActivity(intent);
//                            }
//                        })
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)

                        .init();

            }
        }.execute(null, null, null);
    }

}
