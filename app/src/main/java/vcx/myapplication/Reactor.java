package vcx.myapplication;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import android.util.Log;
import android.content.Context;

/**
 * Handles information from the Android MQTT client
 */
public class Reactor implements IMqttActionListener {

    // Types of actions this listener can handle
    enum Action {
        CONNECT,
        SUBSCRIBE,
        PUBLISH
    }

    /**
     * Constructor
     * @param context context in which the MQTT action was done
     * @param action the type of action this listener is expected to handle
     */
    public Reactor(Context context, Action action)
    {
        this.action = action;
        this.context = context;
        this.subbed = false;
    }

    /**
     * Constructor
     * @param context context in which the MQTT action was done
     * @param action the type of action this listener is expected to handle
     * @param subbed If true, a subscribe message will not be sent after connect
     */
    public Reactor(Context context, Action action, boolean subbed)
    {
        this.action = action;
        this.context = context;
        this.subbed = subbed;
    }

    private Context context;
    private Action action;
    private boolean subbed;

    @Override
    public void onSuccess(IMqttToken token)
    {
        switch(action)
        {
            case CONNECT:
                handleConnect(token);
            break;
            case PUBLISH:
                handlePublish();
            break;
            case SUBSCRIBE:
                handleSubscribe();
            break;
            default:
                Log.d("Reactor", "Unknown MQTT Action");
        }
    }
    @Override
    public void onFailure(IMqttToken token, Throwable exception)
    {
        switch(action)
        {
            case CONNECT:
                handleConnect(token, exception);
                break;
            case PUBLISH:
                handlePublish(exception);
            break;
            case SUBSCRIBE:
                handleSubscribe(exception);
            break;
            default:
                Log.d("Reactor", "Unknown MQTT Action");
        }
    }

    /**
     * An MQTT connection has been successfully established
     */
    private void handleConnect(IMqttToken token) {
        Log.d("Reactor", "MQTT Connected");
        // Subscribe to topic
        if (!subbed) {
            try {
                this.action = Action.SUBSCRIBE; //We now monitor a diff. action with the same Reactor
                token.getClient().subscribe(context.getResources().getString(R.string.mqtt_topic), 2,
                                            context, this);
            }
            catch(MqttException e) {
                Log.d("MQTTErr", e.getMessage());
            }
        }
    }

    /**
     * An MQTT connection failed. Attempt to reconnect, using the same Listener
     * @param exception The exception to throw (won't actually be thrown)
     */
    private void handleConnect(IMqttToken token, Throwable exception)
    {
        Log.d("Reactor", exception.getMessage());
        exception.printStackTrace();
        try
        {
            token.getClient().connect().setActionCallback(this);
        }
        catch(Exception e)
        {
            Log.d("Mqtt.connect", e.getMessage());
        }
    }

    private void handleSubscribe() {
        Log.d("Reactor", "Subscribed to a topic");
        subbed = true;
    }

    private void handleSubscribe(Throwable exception) {
        Log.d("Reactor", exception.getMessage());
        subbed = false;
    }

    /**
     * Payload has been published to broker
     */
    private void handlePublish() {
        Log.d("Reactor", "Payload published");
    }

    /**
     * An MQTT PUBLISH action failed. Tell the user
     */
    private void handlePublish(Throwable exception)
    {
        Log.d("Reactor", exception.getMessage());
    }
}
