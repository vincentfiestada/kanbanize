package vcx.myapplication;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.provider.Settings.Secure;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Implements the MqttCallback and reacts to MqttAndroidClient events
 * (i.e. events not initiated by the local client, ex. message arrival, disconnection, etc.)
 */
public class MQTTMonitor implements MqttCallback {

    public MQTTMonitor(Context c) {
        this.context = c;
        aid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    private Context context;
    private String aid;

    @Override
    public void connectionLost(Throwable cause) {
        MQTTClient.reconnect(); // Attempt to reconnect
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        JSONObject payload = new JSONObject(message.toString());
        Log.d("rcv", payload.toString());
        // Make sure it isn't from us
        if (!payload.isNull("from") && !payload.get("from").equals(aid)) {
            // React to notification
            Log.d("rcv", "Opening toast");
            Log.d("rcv", payload.getString("action"));
            if (payload.getString("action").compareTo("MOVE") == 0) {
                // Move task
                Log.d("go", "Moving");
                JSONArray params = payload.getJSONArray("params");

                TaskListAdapter src = MainActivity.getAdapterByStatus(params.getString(0));
                TaskListAdapter dest = MainActivity.getAdapterByStatus(params.getString(1));

                if (src != null && dest != null) {
                    // Remove Task from source and add to destination
                    Task t = src.removeById(payload.getInt("id"));
                    if (t != null) {
                        // Make sure data binding is updated
                        src.notifyDataSetChanged();
                        t.setStatus(params.getString(1));
                        // Notify user
                        Notify.notif(this.context, "Message", context.getString(R.string.notif_moved_text, t.getName(), t.getStatus()));
                        dest.getList().add(t);
                        dest.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("MQTT", "Delivery complete for message with token " + token.toString());
    }
}
