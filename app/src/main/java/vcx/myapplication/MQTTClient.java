package vcx.myapplication;

import android.util.Log;
import android.content.Context;
import android.provider.Settings.Secure;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Provides static methods for MQTT transactions
 */
public class MQTTClient {

    public enum Actions {
        MOVE,
        DELETE,
        EDIT,
        ASSIGN,
        MARK
    }

    public MQTTClient(Context c, String serverURL, String name) {
        if (client == null) {
            // Create MQTT Client
            context = c;
            aid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            client = new MqttAndroidClient(context, serverURL, name);
            client.setCallback(new MQTTMonitor(context));
            try
            {
                client.connect(new MqttConnectOptions(),
                        new Reactor(context.getApplicationContext(), Reactor.Action.CONNECT));
            }
            catch(Exception e)
            {
                Log.d("MQTT", e.getMessage());
            }
        }
    }


    private static MqttAndroidClient client;
    private static Context context;
    private static String aid;

    public static void reconnect() {
        if (client != null && !client.isConnected()) {
            try {
                client.connect(new MqttConnectOptions(),
                    new Reactor(context.getApplicationContext(), Reactor.Action.CONNECT));
            }
            catch(Exception e) {
                Log.d("MQTTErr", e.getMessage());
            }
        }
    }

    public static void publishStatusChange(long id, Task.Status oldStatus, Task.Status newStatus)
    {
        if (client != null && client.isConnected())
        {
            try {
                // Construct payload
                JSONObject payload = new JSONObject();
                payload.put("action", Actions.MOVE.toString());
                payload.put("id", id);
                payload.put("from", aid);
                ArrayList<String> params = new ArrayList<>();
                params.add(oldStatus.toString());
                params.add(newStatus.toString());
                payload.put("params", params);
                client.publish(context.getResources().getString(R.string.mqtt_topic), new MqttMessage(payload.toString().getBytes()),
                        context.getApplicationContext(),
                        new Reactor(context.getApplicationContext(), Reactor.Action.PUBLISH));
            }
            catch(Exception e) {
                Log.d("MQTTErr", e.getMessage());
            }
        }
    }
}
