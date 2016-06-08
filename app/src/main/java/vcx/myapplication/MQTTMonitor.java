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

                TaskListAdapter src = MainActivity.getAdapterByStatus(payload.getString("oldStatus"));
                TaskListAdapter dest = MainActivity.getAdapterByStatus(payload.getString("newStatus"));

                if (src != null && dest != null) {
                    // Remove Task from source and add to destination
                    Task t = src.removeById(payload.getInt("id"));
                    if (t != null) {
                        // Make sure data binding is updated
                        src.notifyDataSetChanged();
                        t.setStatus(Task.toStatus(payload.getString("newStatus")));
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
                    String taskName = payload.getString("name");
                    Task.Status status = Task.toStatus(payload.getString("status"));
                    int userId = payload.getInt("uid");

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
            else if (action.equals(Actions.EDIT.toString())) {
                // Add task
                Log.d("MQTT", "Adding a Task");

                try {
                    String taskName = payload.getString("name");
                    Task.Status status = Task.toStatus(payload.getString("status"));
                    int userId = payload.getInt("uid");
                    int taskId = payload.getInt("id");

                    Task task = Tasks.getById(taskId);
                    if (task == null) return;
                    User user = Users.getById(userId);
                    if (user == null) return;

                    task.setName(taskName);
                    task.setUser(user);

                    TaskListAdapter dest = MainActivity.getAdapterByStatus(status);
                    if (dest != null) {
                        // Notify user
                        Notify.notify(this.context, "Task edited",
                                context.getString(R.string.notif_edited_text,
                                        task.getId()));
                        dest.notifyDataSetChanged();
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
                    Task.Status status = Task.toStatus(payload.getString("status"));

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
                    Log.d("MQTTErr", e.getMessage());
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("MQTT", "Delivery complete for message with token " + token.toString());
    }
}
