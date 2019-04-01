package android.example.com.squawker.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = SquawkFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        Log.d(LOG_TAG, "Refreshed Token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Send token to your app server
    }
}
