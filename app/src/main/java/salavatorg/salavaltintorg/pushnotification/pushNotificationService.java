package salavatorg.salavaltintorg.pushnotification;
import android.util.Log;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;


/**
 * Created by masoomeh on 1/20/18.
 */
public class pushNotificationService  extends NotificationExtenderService {

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read properties from result.

        // Returning true tells the OneSignal SDK you have processed the notification and not to display it's own.
        Log.i("tag", "isAppInFocus : " + receivedResult.isAppInFocus + " restoring: " + receivedResult.restoring
                + " payload: " + receivedResult.payload.toJSONObject() + " additional: " + receivedResult.payload.additionalData + " title: " + receivedResult.payload.title
                + " body: " + receivedResult.payload.body);
        NotificationsClass notificationsClass = new NotificationsClass();
        try {
            notificationsClass.NotificationShow(this, receivedResult.payload.title, receivedResult.payload.body,
                    receivedResult.payload.additionalData.getString("header"),
                    receivedResult.payload.additionalData.getString("body"),
                    receivedResult.payload.additionalData.getString("footer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}

//    public void CallNotification(String title , String body){
//        NotificationCompat.Builder builder =
//            new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.splash)
//                    .setContentTitle(title)
//                    .setContentText(body);
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//
//        // Add as notification
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
//    }


