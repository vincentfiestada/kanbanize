package vcx.myapplication;

import android.content.Context;
import android.util.Log;
import android.provider.Settings.Secure;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vcx.myapplication.MQTTClient.Actions;

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
        // Make sure it isn't from us
        Log.d("rcv", payload.toString());
        if (!payload.isNull("from") && !payload.get("from").equals(aid)) {
            // React to notification
            String action = payload.getString("action");
            if (action.equals(Actions.MOVE.toString())) {
                // Move task
                Log.d("MQTT", "Moving a task");
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
                        Notify.notify(this.context, "Task moved", context.getString(R.string.notif_moved_text, t.getName(), t.getStatus()));
                        dest.getList().add(t);
                        dest.notifyDataSetChanged();
                    }
                }
            }
            else if (action.equals(Actions.ADD.toString())) {
                // Add task
                Log.d("MQTT", "Adding a Task");

                try {
                    JSONArray params = payload.getJSONArray("params");
                    String taskName = params.getString(0);
                    Task.Status status = Task.toStatus(params.getString(1));
                    int userId = params.getInt(2);

                    Task newTask = new Task(payload.getInt("id"), taskName, Users.getById(userId), status);

                    TaskListAdapter dest = MainActivity.getAdapterByStatus(status);
                    if (dest != null) {
                        if (dest.getList().add(newTask)) {
                            // Notify user
                            Notify.notify(this.context, "Task added",
                                    context.getString(R.string.notif_added_text,
                                                        newTask.getName(),
                                                        newTask.getUser().getName()));
                            dest.notifyDataSetChanged();
                        }
                    }
                }
                catch(JSONException e) {
                    Log.d("MQTTErr", "Wrong Packet format");
                }
            }
            else if (action.equals(Actions.DELETE.toString())) {
                // Add task
                Log.d("MQTT", "Deleting a Task");

                try {
                    JSONArray params = payload.getJSONArray("params");
                    Task.Status status = Task.toStatus(params.getString(0));

                    TaskListAdapter target = MainActivity.getAdapterByStatus(status);
                    if (target != null) {
                        Task t = target.removeById(payload.getInt("id"));
                        if (t != null) {
                            // Notify user
                            Notify.notify(this.context, "Task removed", context.getString(R.string.notif_removed_text, t.getName()));
                            target.notifyDataSetChanged();
                        }
                    }
                }
                catch(JSONException e) {
                    Log.d("MQTTErr", "Wrong Packet format");
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("MQTT", "Delivery complete for message with token " + token.toString());
    }
}
