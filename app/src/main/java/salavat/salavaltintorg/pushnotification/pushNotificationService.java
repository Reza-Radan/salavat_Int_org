package salavat.salavaltintorg.pushnotification;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import salavat.salavaltintorg.dao.AppCreatorDatabase;
import salavat.salavaltintorg.dao.Notification;


/**
 * Created by masoomeh on 1/20/18.
 */

    public class pushNotificationService extends FirebaseMessagingService {
        private static final String TAG = "pushNotificationService";
    private AppCreatorDatabase db;
    private Notification notification;
    String googleId = "GOOGLEID";
    String token = "token";

    @Override
    public void onNewToken(String s) {


        Log.e("NEW_TOKEN", s);
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + s);

        SharedPreferences sharedPreferences = getSharedPreferences(googleId,MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(token,s);
        edit.commit();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
//        Toast.makeText(getApplicationContext() ,"notification",Toast.LENGTH_LONG).show();

        NotificationsClass notificationsClass = new NotificationsClass();
        Log.w(TAG, "remoteMessage: "+remoteMessage.getData());
        Map<String, String> data = remoteMessage.getData();

        notificationsClass.NotificationShow(this,data.get("title"), data.get("brief"),
                data.get("header"),
                data.get("body"),
                data.get("footer"));

        notification = new Notification();
        notification.setTitle(data.get("header"));
        notification.setDesc(data.get("body"));
        notification.setFooter(data.get("footer"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        notification.setDate(currentDateandTime);
        db = Room.databaseBuilder(
                this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();
        getNotificationData();
    }

    public void getNotificationData() {
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... voids) {
                db.notificationDao().insertOnlySingleRecord(notification);

                return null;
            }
        }.execute();
    }
// Returning true tells the OneSignal SDK you have processed the notification and not to display it's own.
//            Log.i("tag", "isAppInFocus : " + receivedResult.isAppInFocus + " restoring: " + receivedResult.restoring
//                + " payload: " + receivedResult.payload.toJSONObject() + " additional: " + receivedResult.payload.additionalData + " title: " + receivedResult.payload.title
//                + " body: " + receivedResult.payload.body);
//
//
//
//    NotificationsClass notificationsClass = new NotificationsClass();
//        try {
//        Log.w("tag", " body: "+receivedResult.payload.additionalData.getString("body"));
//
//        notificationsClass.NotificationShow(this, receivedResult.payload.title, receivedResult.payload.body,
//                receivedResult.payload.additionalData.getString("header"),
//                receivedResult.payload.additionalData.getString("body"),
//                receivedResult.payload.additionalData.getString("footer"));
//
//        notification = new Notification();
//        notification.setTitle(receivedResult.payload.additionalData.getString("header"));
//        notification.setDesc(receivedResult.payload.additionalData.getString("body"));
//        notification.setFooter(receivedResult.payload.additionalData.getString("footer"));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        String currentDateandTime = sdf.format(new Date());
//        notification.setDate(currentDateandTime);
//        db = Room.databaseBuilder(
//                this, AppCreatorDatabase.class, AppCreatorDatabase.DB_NAME).build();
//        getNotificationData();
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//        return true;

}
