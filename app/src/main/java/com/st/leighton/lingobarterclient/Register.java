package com.st.leighton.lingobarterclient;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    Context baseContext;

    Button registerB;
    Button showPasswordB;
    Button loginB;

    EditText usernameET;
    EditText emailET;
    EditText passwordET;

    Boolean passwordFlag = Boolean.TRUE;

    final public static String EMAIL_KEY = "REGISTER_EMAIL";
    final public static String PASSWORD_KEY = "REGISTER_PASSWORD";

    final private String hidePassword = "HIDE PASSWORD", showPassword = "SHOW PASSWORD";
    String username = "", email = "", password = "";

    Webservice socketService;
    BroadcastReceiver noticeReceiver;

    ProgressDialog waitIndicator;
    final public static String REGISTER_FEEDBACK = "REGISTER_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.Register");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                waitIndicator.cancel();
                String message = intent.getStringExtra(REGISTER_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Bundle passToEmailConfirmation = new Bundle();
                        passToEmailConfirmation.putString(EMAIL_KEY, email);
                        passToEmailConfirmation.putString(PASSWORD_KEY, password);

                        Intent confirmIntent = new Intent(baseContext, EmailConfirmation.class);
                        confirmIntent.putExtras(passToEmailConfirmation);
                        startActivity(confirmIntent);
                        finish();
                        break;

                    case "EmailTaken":
                        Toast.makeText(baseContext,"This name (or email) has been registered", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_register);

        baseContext = this;

        socketService = Webservice.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

        registerB = (Button) findViewById(R.id.hx_register_button_register);
        showPasswordB = (Button) findViewById(R.id.hx_register_button_show_password);
        loginB = (Button) findViewById(R.id.hx_register_button_login);

        usernameET = (EditText) findViewById(R.id.hx_register_edit_user_name);
        emailET = (EditText) findViewById(R.id.hx_register_edit_email);
        passwordET = (EditText) findViewById(R.id.hx_register_edit_password);

        loginB.setPaintFlags(loginB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        showPasswordB.setPaintFlags(showPasswordB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        usernameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        emailET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        passwordET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                username = usernameET.getText().toString();
                email = emailET.getText().toString();
                password = passwordET.getText().toString();

                if (username.equals("")) {
                    Toast.makeText(baseContext,"Please input username", Toast.LENGTH_LONG).show();
                    usernameET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    return ;
                }

                if (!isEmailValid(email)) {
                    Toast.makeText(baseContext,"Please check email", Toast.LENGTH_LONG).show();
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
                        socketService.Register(username, email, password);
                    }
                }).start();
            }
        });

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, Login.class);
                startActivity(intent);
                finish();
            }
        });

        showPasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                if(passwordFlag) {
                    showPasswordB.setText(hidePassword);
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordET.setSelection(passwordET.getText().length());
                    passwordFlag = false;
                } else {
                    showPasswordB.setText(showPassword);
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordET.setSelection(passwordET.getText().length());
                    passwordFlag = true;
                }
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    void resetBackgroundColors() {
        usernameET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        emailET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        passwordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }
}
