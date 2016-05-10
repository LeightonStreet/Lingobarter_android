package com.st.leighton.lingobarterclient;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelfInformation extends AppCompatActivity {

    Context baseContext;

    EditText taglineET;
    EditText biographyET;

    Button updateB;
    Button cancelB;

    String tagline, biography;

    Websocket socketService;
    BroadcastReceiver noticeReceiver;

    ProgressDialog waitIndicator;
    final public static String SELF_INFORMATION_UPDATE_FEEDBACK = "SELF_INFORMATION_UPDATE_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.SelfInformation");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                waitIndicator.cancel();
                String message = intent.getStringExtra(SELF_INFORMATION_UPDATE_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Toast.makeText(baseContext,"Self introduction has been updated", Toast.LENGTH_LONG).show();
                        break;

                    case "ERROR":
                        Toast.makeText(baseContext,"Cannot connect to server, please check your network", Toast.LENGTH_LONG).show();
                        break;

                    default:

                        break;
                }
            }
        };
        this.registerReceiver(noticeReceiver, noticeFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.noticeReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_information);

        baseContext = this;

        socketService = Websocket.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

        taglineET = (EditText) findViewById(R.id.hx_self_information_edit_tagline);
        biographyET = (EditText) findViewById(R.id.hx_self_information_edit_biography);

        updateB = (Button) findViewById(R.id.hx_self_information_button_submit);
        cancelB = (Button) findViewById(R.id.hx_self_information_button_cancel);

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagline = taglineET.getText().toString();
                biography = biographyET.getText().toString();

                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                waitIndicator.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.UpdateSelfInformation(tagline, biography);
                    }
                }).start();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
