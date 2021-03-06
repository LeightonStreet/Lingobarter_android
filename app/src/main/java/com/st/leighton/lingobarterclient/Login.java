package com.st.leighton.lingobarterclient;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.st.leighton.util.MyProperty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Login extends AppCompatActivity {

    Context baseContext;

    Button logginB;
    Button forgetPasswordB;
    Button registerB;

    EditText emailET;
    EditText passwordET;

    String email = "", password = "";

    Webservice socketService;
    BroadcastReceiver noticeReceiver;

    ProgressDialog waitIndicator;
    final public static String LOGIN_FEEDBACK = "LOGIN_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.Login");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                waitIndicator.cancel();
                String message = intent.getStringExtra(LOGIN_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Intent main_intent = new Intent(baseContext, MainActivity.class);
                        startActivity(main_intent);
                        finish();
                        break;

                    case "Uncomplete":
//                        Intent basic_profile_intent = new Intent(baseContext, BasicProfile.class);
//                        startActivity(basic_profile_intent);
//                        finish();
                        Intent main1_intent = new Intent(baseContext, MainActivity.class);
                        startActivity(main1_intent);
                        finish();
                        break;

                    case "NeedConfirm":
                        Bundle passToEmailConfirmation = new Bundle();
                        passToEmailConfirmation.putString(Register.EMAIL_KEY, email);
                        passToEmailConfirmation.putString(Register.PASSWORD_KEY, password);

                        Intent confirm_intent = new Intent(baseContext, EmailConfirmation.class);
                        confirm_intent.putExtras(passToEmailConfirmation);
                        startActivity(confirm_intent);
                        finish();
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
        this.setContentView(R.layout.activity_login);

        try {
            loadPropties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent webSocketServiceIntent = new Intent(this, Webservice.class);
        startService(webSocketServiceIntent);

        baseContext = this;

        socketService = Webservice.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

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
//                email = "lingobarter.user1@gmail.com";
//                password = "user1lingobarter";

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

                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                waitIndicator.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.Login(email, password);
                    }
                }).start();
            }
        });

        forgetPasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                Intent intent = new Intent(baseContext, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                Intent intent = new Intent(baseContext, Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void resetBackgroundColors() {
        emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        passwordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }

    private void loadPropties() throws IOException {
        String[] fileList = { "custom-settings.properties", "settings.properties"  };
        for (int i = fileList.length - 1; i >= 0; i--) {
            String file = fileList[i];
            try {
                InputStream fileStream = getAssets().open(file);
                MyProperty.load(fileStream);
            } catch (FileNotFoundException e) {
                Log.d("Warning", "Ignoring missing property file " + file);
            }
        }
    }
}
