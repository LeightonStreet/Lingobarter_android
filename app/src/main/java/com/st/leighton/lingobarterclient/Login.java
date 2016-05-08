package com.st.leighton.lingobarterclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Context baseContext;

    Button logginB;
    Button forgetPasswordB;
    Button registerB;

    EditText emailET;
    EditText passwordET;

    String email = "", password = "";

    private BroadcastReceiver noticeReceiver;
    final public static String LOGIN_FEEDBACK = "LOGIN_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.Login");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(LOGIN_FEEDBACK);
                Log.i("Here", message);
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
        this.setContentView(R.layout.activity_login);

        baseContext = this;

        logginB = (Button) findViewById(R.id.hx_login_button_login);
        forgetPasswordB = (Button) findViewById(R.id.hx_login_button_forget_password);
        registerB = (Button) findViewById(R.id.hx_login_button_register);

        emailET = (EditText) findViewById(R.id.hx_login_edit_email);
        passwordET = (EditText) findViewById(R.id.hx_login_edit_password);

        forgetPasswordB.setPaintFlags(forgetPasswordB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registerB.setPaintFlags(registerB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        logginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if (email.equals("")) {
                    Toast.makeText(baseContext,"Please input email", Toast.LENGTH_LONG).show();
                    emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    return ;
                }

                if (password.equals("")) {
                    Toast.makeText(baseContext,"Please input password", Toast.LENGTH_LONG).show();
                    passwordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    return ;
                }

                Intent intent = new Intent(baseContext, MainActivity.class);
                startActivity(intent);
            }
        });

        forgetPasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                Intent intent = new Intent(baseContext, ForgetPassword.class);
                startActivity(intent);
            }
        });

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                Intent intent = new Intent(baseContext, Register.class);
                startActivity(intent);
            }
        });

        Intent webSocketServiceIntent = new Intent(this, Websocket.class);
        startService(webSocketServiceIntent);
    }

    void resetBackgroundColors() {
        emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        passwordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }
}
