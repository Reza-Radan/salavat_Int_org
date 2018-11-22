//package salavat.salavaltintorg.pushnotification;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
///**
// * Created by masoomeh on 1/20/18.
// */
//
//public class FirebaseIDService extends FirebaseInstanceIdService {
//    private static final String TAG = "FirebaseIDService";
//    public static String googleId = "GOOGLEID";
//    public static String token = "token";
//
//
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        SharedPreferences sharedPreferences = getSharedPreferences(googleId,MODE_PRIVATE);
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        edit.putString(token,refreshedToken);
//        edit.commit();
//        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
//    }
//
//    /**
//     * Persist token to third-party servers.
//     *
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//        // Add custom implementation, as needed.
//    }
//}