package com.st.leighton.lingobarterclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CHATTING_TEST extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting__test);

        final TextView replyTV = (TextView) findViewById(R.id.reply_TV);
        final EditText inputET = (EditText) findViewById(R.id.input_ET);
        Button sendB = (Button) findViewById(R.id.send_B);

        IntentFilter responseFilter = new IntentFilter("android.intent.action.BotChat");
        BroadcastReceiver responseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String response = intent.getStringExtra("RESPONSE");
                String history = replyTV.getText().toString() + "\n" + response;
                replyTV.setText(history);
            }
        };
        this.registerReceiver(responseReceiver, responseFilter);

        if (sendB != null) {
            sendB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String input = inputET.getText().toString();
                    inputET.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Webservice.botchat(input);
                        }
                    }).start();
                }
            });
        }
    }
}
