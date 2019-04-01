package android.example.com.squawker.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Map;

public class NotificationUtil {

    private static final String NOTIFICATION_CHANNEL_ID = "android.example.com.squawker.Squawks";
    private static final String NOTIFICATION_CHANNEL_NAME = "Squawks";

    private static final String NOTIFICATION_ID = "notification_id";

    public static void showNotification(Context context, Map<String, String> data) {
        createNotificationChannel(context);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(getNotificationId(context), buildNotification(context, data));
    }

    private static int getNotificationId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentNotificationId = preferences.getInt(NOTIFICATION_ID, -1);
        if (currentNotificationId == -1) {
            currentNotificationId = 1;
        } else {
            currentNotificationId += 1;
            SharedPreferences.Editor editor = preferences.edit();
            editor.apply();
            editor.putInt(NOTIFICATION_ID, currentNotificationId);
            editor.commit();
        }
        return currentNotificationId;
    }

    private static Notification buildNotification(Context context, Map<String, String> data) {
        String author = data.get(SquawkContract.COLUMN_AUTHOR);
        String message = data.get(SquawkContract.COLUMN_MESSAGE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_duck);
        builder.setContentTitle(author);
        builder.setContentText(message);
        builder.setContentIntent(getPendingIntent(context));
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        return builder.build();
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(intent);
        return taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }
        }
    }
}
