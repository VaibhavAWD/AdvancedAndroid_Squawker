package android.example.com.squawker.fcm;

import android.content.ContentValues;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        saveSquawkToDatabase(data);
        NotificationUtil.showNotification(getApplicationContext(), data);
    }

    private void sendRegistrationToServer(String token) {
        // Send token to your app server
    }

    private void saveSquawkToDatabase(Map<String, String> data) {
        String author = data.get(SquawkContract.COLUMN_AUTHOR);
        String author_key = data.get(SquawkContract.COLUMN_AUTHOR_KEY);
        String message = data.get(SquawkContract.COLUMN_MESSAGE);
        String date = data.get(SquawkContract.COLUMN_DATE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(SquawkContract.COLUMN_AUTHOR, author);
        contentValues.put(SquawkContract.COLUMN_AUTHOR_KEY, author_key);
        contentValues.put(SquawkContract.COLUMN_MESSAGE, message);
        contentValues.put(SquawkContract.COLUMN_DATE, date);

        Uri newSquawkUri = getApplicationContext().getContentResolver()
                .insert(SquawkProvider.SquawkMessages.CONTENT_URI, contentValues);

        if (newSquawkUri == null) {
            Log.d(LOG_TAG, "Failed to save squawk");
        }
    }
}
