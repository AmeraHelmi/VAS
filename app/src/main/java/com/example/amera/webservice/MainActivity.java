package com.example.amera.webservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by amera on 3/1/16.
 */
public class MainActivity extends Activity implements GetJsonString {

    ArrayList<String> paramters; // to pass parameters in url
    ArrayList<String> values;   // to pass values to these parameters
    public EditText usernameField, passwordField;
    public TextView status;  // to show status
    public Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        paramters = new ArrayList<>(); // initialize parameters arraylist
        values = new ArrayList<>();   //  initalize values arraylist
        usernameField = (EditText) findViewById(R.id.usr);
        passwordField = (EditText) findViewById(R.id.pass);
        status = (TextView) findViewById(R.id.textView6);
        btn = (Button) findViewById(R.id.button1);

        // make object from ConnectionManager class to pass parameters and values
        final ConnectionManager connectionManager = new ConnectionManager(MainActivity.this);
        connectionManager.getJsonString = this;
        paramters.add("id");
        paramters.add("first_names");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                values.add(passwordField.getText().toString());
                values.add(usernameField.getText().toString());
                try {
                    connectionManager.connectionManagerGet("http://vas.nile-crocodile.com/ws.php", paramters, values, "Flag for multi web service");
                    // flag if more than one webservice is used
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // implement function of "GetJsonString" interface
    @Override
    public void getData(String output, String connectionFlag) throws JSONException {
        JSONObject userInfo = new JSONObject(output);
        String msg = userInfo.getString("msg");
        String user_id = userInfo.getString("id");
        Toast.makeText(MainActivity.this, msg,
                Toast.LENGTH_LONG).show();
        status.setText(msg);
        AlertDialog levelDialog;
        final CharSequence[] items = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 "};

        if (msg.equals("you are login")) {
            // if username and password matched , open another activity and send user_id as parameter
            Intent i=new Intent(this,login.class);
            i.putExtra("user_id",user_id);
            startActivity(i);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");
//
//            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
//                }
//            });
//
//            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    finish();
//                }
//            });
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        }
        }
//paramters.clear();
//        values.clear();
    }
}