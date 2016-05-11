package com.st.leighton.lingobarterclient;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EmailConfirmation extends AppCompatActivity {

    Context baseContext;

    TextView emailTV;

    Button resendB;
    Button confirmedB;

    AlertDialog.Builder confirmAlertDialogBuilder;

    String email;
    String password;

    Webservice socketService;
    BroadcastReceiver noticeReceiver, loginNoticeReceiver;

    ProgressDialog waitIndicator;
    final public static String EMAIL_CONFIRMATION_FEEDBACK = "EMAIL_CONFIRMATION_FEEDBACK";
    final public static String EMAIL_CONFIRMATION_LOGIN_FEEDBACK = "EMAIL_CONFIRMATION_LOGIN_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.EmailConfirmation");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (waitIndicator.isShowing()) {
                    waitIndicator.cancel();
                }
                String message = intent.getStringExtra(EMAIL_CONFIRMATION_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Toast.makeText(baseContext,"Email has been sent", Toast.LENGTH_LONG).show();
                        break;

                    case "InvalidUser":
                        Toast.makeText(baseContext,"This email has not been registered", Toast.LENGTH_LONG).show();
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

        IntentFilter loginNoticeFilter = new IntentFilter("android.intent.action.EmailConfirmationLogin");
        loginNoticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (waitIndicator.isShowing()) {
                    waitIndicator.cancel();
                }
                String message = intent.getStringExtra(EMAIL_CONFIRMATION_LOGIN_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Intent basicProfile_intent = new Intent(baseContext, BasicProfile.class);
                        startActivity(basicProfile_intent);
                        finish();
                        break;

                    case "ERROR":
                        Toast.makeText(baseContext,"Cannot connect to server, please check your network", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        AlertDialog confirmAlertDialog = confirmAlertDialogBuilder.create();
                        confirmAlertDialog.show();
                        break;
                }
            }
        };
        this.registerReceiver(loginNoticeReceiver, loginNoticeFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.noticeReceiver);
        this.unregisterReceiver(this.loginNoticeReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        baseContext = this;

        socketService = Webservice.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

        emailTV = (TextView) findViewById(R.id.hx_email_text_email);

        resendB = (Button) findViewById(R.id.hx_email_button_resend);
        confirmedB = (Button) findViewById(R.id.hx_email_button_confirm);

        confirmAlertDialogBuilder = new AlertDialog.Builder(this);
        confirmAlertDialogBuilder
                .setTitle("Account Not Activated")
                .setMessage("Your email account has not been activated, please check your email.")
                .setNegativeButton("RESEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        waitIndicator.setMessage("Please wait...");
                        waitIndicator.setCancelable(false);
                        if (!waitIndicator.isShowing()) {
                            waitIndicator.show();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                socketService.Confirm(email);
                            }
                        }).start();
                    }
                })
                .setPositiveButton("BACK TO LOGIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(baseContext, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

        Bundle extra = getIntent().getExtras();
        if(extra == null) {
            email = "";
        } else {
            email = extra.getString(Register.EMAIL_KEY);
            password = extra.getString(Register.PASSWORD_KEY);
        }

        emailTV.setText(email);

        resendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                if (!waitIndicator.isShowing()) {
                    waitIndicator.show();
                }
                Log.d("123", "Arrived");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.Confirm(email);
                    }
                }).start();
            }
        });

        confirmedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                if (!waitIndicator.isShowing()) {
                    waitIndicator.show();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.EmailConfirmationLogin(email, password);
                    }
                }).start();
            }
        });
    }
}
