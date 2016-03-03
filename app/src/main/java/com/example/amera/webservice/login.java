package com.example.amera.webservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amera on 3/1/16.
 */

public class login extends Activity implements GetJsonString {

    ArrayList<String> paramters; // to pass parameters in url
    ArrayList<String> values;   // to pass values to these parameters
    Timer timer;
    TimerTask timerTask;
    public  TextView tv;
    public Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tv=(TextView)findViewById(R.id.textView1);
        btn=(Button)findViewById(R.id.button1);
        Timer myTimer = new Timer();
        MyTimerTask myTask = new MyTimerTask();
        myTimer.schedule(myTask, 3000, 60000);
        Intent myIntent = getIntent(); // gets the previously created intent
        final String userid = myIntent.getStringExtra("user_id");
        paramters = new ArrayList<>(); // initialize parameters arraylist
        values = new ArrayList<>();   //  initalize values arraylist
        // make object from ConnectionManager class to pass parameters and values
        final ConnectionManager connectionManager = new ConnectionManager(login.this);
        connectionManager.getJsonString = this;
        paramters.add("id");
        paramters.add("pain");
        paramters.add("record");
        AlertDialog levelDialog;
        final CharSequence[] items = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 "};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Pain level: ");

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                switch (item) {
                    case 0:
                        // Your code when first option seletced
                        Toast.makeText(login.this, "you select " + String.valueOf(item + 1),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        // Your code when 2nd  option seletced
                        Toast.makeText(login.this, "you select " +String.valueOf(item + 1),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        // Your code when 3rd option seletced
                        Toast.makeText(login.this, "you select " + String.valueOf(item + 1),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        // Your code when 4th  option seletced
                        Toast.makeText(login.this, "you select " + String.valueOf(item + 1),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(login.this, "you select " + String.valueOf(item + 1),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        Toast.makeText(login.this, "you select " + String.valueOf(item + 1),
                                Toast.LENGTH_LONG).show();
                        break;

                }
                dialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                values.add(userid);
//                values.add(String.valueOf(40));
//                values.add(String.valueOf(8));
//                try
//                {
//                    connectionManager.connectionManagerGet("http://vas.nile-crocodile.com/ins.php", paramters, values, "Flag for multi web service");
//                    // flag if more than one webservice is used
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });
//        tv.setText(userid);

    }

    @Override
    public void getData(String output, String connectionFlag) throws JSONException {
        JSONObject userInfo = new JSONObject(output);
        String msg = userInfo.getString("msg");
//        String user_id=userInfo.getString("id");
        Toast.makeText(login.this, msg,
                Toast.LENGTH_LONG).show();
        tv.setText(msg);
//        if (msg.equals("you are login"))
//        {
//            // if username and password matched , open another activity and send user_id as parameter
//            Intent i=new Intent(this,login.class);
//            i.putExtra("user_id",user_id);
//            startActivity(i);
//        }

    }
    class MyTimerTask extends TimerTask {
        public void run() {
            // ERROR

            // how update TextView in link below
            // http://android.okhelp.cz/timer-task-timertask-run-cancel-android-example/

            Toast.makeText(login.this, "this is timer",
                    Toast.LENGTH_LONG).show();
        }
    }


}
