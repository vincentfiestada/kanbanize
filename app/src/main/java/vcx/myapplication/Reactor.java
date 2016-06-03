package vcx.myapplication;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import android.util.Log;
import android.content.Context;

/**
 * Handles information from the Android MQTT client
 */
public class Reactor implements IMqttActionListener {

    // Types of actions this listener can handle
    enum Action {
        CONNECT,
        DISCONNECT,
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
    }

    private Context context;
    private Action action;

    @Override
    public void onSuccess(IMqttToken token)
    {
        switch(action)
        {
            case CONNECT:
                handleConnect();
            break;
            case PUBLISH:
                handlePublish();
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
            default:
                Log.d("Reactor", "Unknown MQTT Action");
        }
    }

    /**
     * An MQTT connection has been successfully established
     */
    private void handleConnect() {
        Log.d("Reactor", "MQTT Connected");
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
        try
        {
            // Show a toast notification
            Notify.toast(context, "DEBUG: Publish Failed", 3000);
        }
        catch(Exception e)
        {
            Log.d("Mqtt.connect", e.getMessage());
        }
    }
}
