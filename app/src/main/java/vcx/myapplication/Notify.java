package vcx.myapplication;

//import java.util.Calendar;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
import android.content.Context;
//import android.content.Intent;
//import android.support.v7.app.NotificationCompat.Builder;
import android.widget.Toast;

/**
 * Static methods for showing toast notifications to the user
 */
public class Notify {

    /**
     * Display a toast notification to the user
     * @param context Context from which to create a notification
     * @param text The text the toast should display
     * @param duration The amount of time for the toast to appear to the user
     */
    static void toast(Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
