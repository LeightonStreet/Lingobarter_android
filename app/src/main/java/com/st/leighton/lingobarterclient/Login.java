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

public class Login extends AppCompatActivity {

    Context baseContext;
    Websocket socketService;

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

                switch (message) {
                    case "Succeed":
                        Intent main_intent = new Intent(baseContext, MainActivity.class);
                        startActivity(main_intent);
                        break;

                    case "NeedConfirm":
                        Intent confirm_intent = new Intent(baseContext, EmailConfirmation.class);
                        confirm_intent.putExtra(Register.EMAIL_KEY, email);
                        startActivity(confirm_intent);
                        break;

                    case "InvalidPassword":
                        Toast.makeText(baseContext,"Wrong password", Toast.LENGTH_LONG).show();
                        passwordET.setText("");
                        passwordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                        break;

                    case "InvalidUser":
                        Toast.makeText(baseContext,"User not exist", Toast.LENGTH_LONG).show();
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
        socketService = Websocket.getInstance();

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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.Login(email, password);
                    }
                }).run();
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

//
        Button god = (Button) findViewById(R.id.hx_login_button_god);
        god.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(baseContext, ApplicationSettings.class);
                startActivity(main_intent);
            }
        });
//
    }

    void resetBackgroundColors() {
        emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        passwordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }
}
