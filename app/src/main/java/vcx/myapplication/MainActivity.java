package vcx.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.util.Log;

import org.eclipse.paho.android.service.*;

import org.eclipse.paho.client.mqttv3.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Start the MqttService
//        this.startService(new Intent(getBaseContext(), MqttService.class));
    }

    private MqttAndroidClient client;

    // TODO: Handle onDragEvent
    // TODO: Create classes for Task, user
    // TODO: import MQTT service

    public void notify(View v)
    {
        // Create new MQTT client
        Log.d("click", "Hi");

        if (this.client == null)
        {
            // Create MQTT Client
            this.client = new MqttAndroidClient(getApplicationContext(), getResources().getString(R.string.broker_uri), "test1");
            try
            {
                client.connect(new MqttConnectOptions(),
                        new Reactor(getBaseContext(), Reactor.Action.CONNECT));
            }
            catch(Exception e)
            {
                Log.d("Err", e.getMessage());
            }
        }

        if (client.isConnected())
        {
            Log.d("Hello", "hi");
        }
    }
}
