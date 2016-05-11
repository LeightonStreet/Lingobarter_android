package com.st.leighton.lingobarterclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassword extends AppCompatActivity {

    Context baseContext;
    WebService socketService;

    EditText emailET;

    Button sendB;
    Button loginB;

    String email;

    private BroadcastReceiver noticeReceiver;
    final public static String FORGET_PASSWORD_FEEDBACK = "FORGET_PASSWORD_SEND_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        baseContext = this;
        socketService = WebService.getInstance();

        emailET = (EditText) findViewById(R.id.hx_forget_edit_email);

        sendB = (Button) findViewById(R.id.hx_forget_button_send);
        loginB = (Button) findViewById(R.id.hx_forget_button_login);

        if (loginB != null) {
            loginB.setPaintFlags(loginB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        emailET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();

                email = emailET.getText().toString();
                if (Register.isEmailValid(email)) {
                    Toast.makeText(baseContext,"Wait...", Toast.LENGTH_LONG).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socketService.ResetPassword(email);
                        }
                    }).start();

                } else {
                    Toast.makeText(baseContext,"Please check your email", Toast.LENGTH_LONG).show();
                    emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                }
            }
        });

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();

                Intent intent = new Intent(baseContext, Login.class);
                startActivity(intent);
            }
        });

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.ForgetPassword");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(FORGET_PASSWORD_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Toast.makeText(baseContext,"Email has been sent", Toast.LENGTH_LONG).show();
                        break;

                    case "InvalidUser":
                        Toast.makeText(baseContext, "User not exist", Toast.LENGTH_LONG).show();
                        emailET.setText("");
                        emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                        break;

                    default:

                        break;
                }
            }
        };
        this.registerReceiver(noticeReceiver, noticeFilter);
    }

    void resetBackgroundColors() {
        emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }
}
