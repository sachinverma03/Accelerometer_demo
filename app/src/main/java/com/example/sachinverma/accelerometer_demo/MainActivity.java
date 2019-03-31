package com.example.sachinverma.accelerometer_demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sManager;
    private Sensor accl;
    private float basex;
    private float basey;
    private float basez;
    private  Toast toast=null;


    private float x;
    private float y;
    private float z;
    private boolean monitor;

//    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

   private SmsManager smsManager;
   private String url;
   private String uid;
   private float delta;

//    private int cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sManager=(SensorManager)this.getSystemService(SENSOR_SERVICE);
        accl=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this, accl, SensorManager.SENSOR_DELAY_NORMAL);
//        smsManager = SmsManager.getDefault();
//        cnt=0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, accl, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        toast.cancel();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView xcor= (TextView)findViewById(R.id.xCor);
        TextView ycor= (TextView)findViewById(R.id.yCOr);
        TextView zcor= (TextView)findViewById(R.id.zCOr);

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        xcor.setText(Float.toString(x));
        ycor.setText(Float.toString(y));
        zcor.setText(Float.toString(z));

        if(monitor==true)
        {
            if(Math.abs(y-basey)>6)
            {
//                sendSMSMessage();
                sendAlertToServer();
                monitor=false;
            }
        }
    }
    protected void sendAlertToServer()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param= new RequestParams();
        param.put("activity","movement detected");
        param.put("id",uid);


        client.post(url, param, new AsyncHttpResponseHandler() {
//        client.get("https://www.google.com", new AsyncHttpResponseHandler() {

                @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                toast = Toast.makeText(getApplicationContext(),
                        "Data sent successfully"+ statusCode, Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                toast = Toast.makeText(getApplicationContext(),
                            "Failed sending data. Error Code: "+ statusCode, Toast.LENGTH_LONG);
                toast.show();


            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setBase(View view)
    {
        EditText urlTextfield=(EditText)findViewById(R.id.url);
        url= urlTextfield.getText().toString();
        basex=x;
        basey=y;
        basez=z;
    monitor=true;

    }

//    protected void sendSMSMessage() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.SEND_SMS)) {
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.SEND_SMS},
//                        MY_PERMISSIONS_REQUEST_SEND_SMS);
//            }
//        }
//        else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.SEND_SMS},
//                    MY_PERMISSIONS_REQUEST_SEND_SMS);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(pno, null, "movement Detected", null, null);
//                    Toast.makeText(getApplicationContext(), "SMS sent.",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//        }
//
//    }
}
