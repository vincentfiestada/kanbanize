package vcx.myapplication;

import android.util.Log;
import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Provides static methods for MQTT transactions
 */
public class MQTTClient {

    public MQTTClient(Context context, String serverURL, String name) {
        if (client == null) {
            // Create MQTT Client
            client = new MqttAndroidClient(context, serverURL, name);
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

    public static void publishStatusChange(long id, Task.Status newStatus)
    {
        if (client != null && client.isConnected())
        {
            try {
                client.publish("vcx.kanban", new MqttMessage("Moved".getBytes()));
            }
            catch(MqttException e) {
                Log.d("ERR", e.getMessage());
            }
        }
    }
}
