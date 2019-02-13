package org.wilsonwang.hagi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import helpers.MQTTHelper;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;

    TextView dataReceived;

    Button button_connection;
    boolean button_clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived = (TextView) findViewById(R.id.dataReceived);
        button_connection = (Button) findViewById(R.id.button_conn);
    }

    private void startMqtt() {
//        if(!mqttHelper.connection_status){
            mqttHelper = new MQTTHelper(getApplicationContext());
            mqttHelper.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {

                }

                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    Log.w("JSON", mqttMessage.toString());
                    String jsonString = mqttMessage.toString();
                    JSONObject reader = new JSONObject(jsonString);
                    String distance = reader.getString("distance");
                    Log.w("JSON", distance);
                    dataReceived.setText(distance);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
//        }
    }

    private void disconnectMQTT() {
//        if(mqttHelper.connection_status) {
//            mqttHelper.connection_status = false;
//
//        }
    }

    public void ButtonClick(View view){
//        Intent buttonIntent = new Intent(FromActivity.this, ToActivity.class);
        if(button_clicked){
            Log.d("myTag", "button clicked. Quit MQTT");
            button_connection.setText("Connect");
            button_clicked = false;
            mqttHelper.disconnect();
        }else{
            startMqtt();
            Log.d("myTag", "connection_status "+mqttHelper.queryConnectionStatus());
            if(mqttHelper.queryConnectionStatus()){
                Log.d("myTag", "button clicked. Start MQTT");
                button_connection.setText("Disconnect");
                button_clicked = true;
            }
        }
    }
}
