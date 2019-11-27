package com.example.assignment2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {

    private Accelerometer acc;
    private PingView pingView;

    private String MQTT_BROKER_IP = "tcp://10.0.2.2:1883";
    private MqttAndroidClient mqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pingView = new PingView(this);
        setContentView(pingView);
        acc = new Accelerometer(this, pingView);
        acc.sensor();

        mqttClient();

    }


    private void mqttClient(){

        // set up client
        mqttClient = new MqttAndroidClient(this, MQTT_BROKER_IP, "lab11client");
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.i("MqttActivity", "MQTT Connected");
                //TODO: subscribe to a topic
                Toast.makeText(getApplicationContext(), "MQTT Connected, subscribing...", Toast.LENGTH_SHORT).show();

                try {
                    mqttClient.subscribe("foo", 0); // 0 - is the QoS value
                    Log.i("MqttActivity", "subscribed!!");

                    // mosquitto_pub -t "foo/bar" -m "Hello, MQTT!"
                    MqttMessage message = new MqttMessage("Hello, I am Android Mqtt Client.".getBytes());
                    message.setQos(2);
                    message.setRetained(false);
                    mqttClient.publish("foo", message);

                }catch (Exception exception){
                    Log.i("MqttActivity", "ERROR CONNECTING");
                }


            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.i("MqttActivity", "MQTT Connection Lost!");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                String message = new String(mqttMessage.getPayload());
                Log.w("MqttActivity","MQTT Message:" + topic +", msg:" + message );

                if(message.equals("left")){
                    pingView.movePaddle(100, pingView.getPlayer2());
                }else if(message.equals("right")){
                    pingView.movePaddle(-100, pingView.getPlayer2());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.i("MqttActivity", "MQTT Message Delivered!");
            }
        });

        try {
            mqttClient.connect();
        }catch (Exception exception){
            Log.i("MqttActivity", "ERROR CONNECTING");
        }
    }



    @Override
    protected void onDestroy() {
        try {
            mqttClient.disconnect();
        }catch (Exception exception){
            Log.i("MqttActivity", "ERROR CONNECTING");
        }

        super.onDestroy();
    }
}


