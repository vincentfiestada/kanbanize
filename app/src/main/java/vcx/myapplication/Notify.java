package vcx.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Static methods for showing toast notifications to the user
 */
public class Notify {

    private static int nextId = 0;

    /**
     * Display a simple informational notification to the user
     * @param context The context from which to send the Intent
     * @param title Title of the notification
     * @param text The text the toast should display
     */
    static void notif(Context context, String title, String text) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle(title)
                        .setContentText(text);

        Intent i = new Intent(context, MainActivity.class);

        // Create artificial back stack: HomeScreen > MainActivity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Add the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Add the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(i);
        PendingIntent p = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(p);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Show Notification
        manager.notify(nextId, mBuilder.build());
        // Get next Id
        nextId = (nextId + 1) % 32000;

    }

}
