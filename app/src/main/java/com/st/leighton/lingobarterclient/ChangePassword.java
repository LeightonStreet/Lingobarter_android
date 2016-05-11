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

public class ChangePassword extends AppCompatActivity {

    Context baseContext;

    EditText oldPasswordET;
    EditText newPasswordET;

    Button showPasswordB;
    Button submitB;
    Button cancelB;

    String oldPassword = "", newPassword = "";
    Boolean passwordFlag = Boolean.TRUE;
    final private String hidePassword = "HIDE PASSWORD", showPassword = "SHOW PASSWORD";

    Webservice socketService;
    BroadcastReceiver noticeReceiver;

    ProgressDialog waitIndicator;
    final public static String CHANGE_PASSWORD_FEEDBACK = "CHANGE_PASSWORD_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.ChangePassword");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                waitIndicator.cancel();
                String message = intent.getStringExtra(CHANGE_PASSWORD_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Toast.makeText(baseContext,"Password has been updated", Toast.LENGTH_LONG).show();
                        finish();
                        break;

                    case "InvalidPassword":
                        Toast.makeText(baseContext,"Old password is wrong", Toast.LENGTH_LONG).show();
                        oldPasswordET.setText("");
                        oldPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
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
        setContentView(R.layout.activity_change_password);

        baseContext = this;

        socketService = Webservice.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

        oldPasswordET = (EditText) findViewById(R.id.hx_change_password_edit_oldpassword);
        newPasswordET = (EditText) findViewById(R.id.hx_change_password_edit_newpassword);

        showPasswordB = (Button) findViewById(R.id.hx_change_password_button_showpassword);
        submitB = (Button) findViewById(R.id.hx_change_password_button_submit);
        cancelB = (Button) findViewById(R.id.hx_change_password_button_cancel);

        showPasswordB.setPaintFlags(showPasswordB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cancelB.setPaintFlags(cancelB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        oldPasswordET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        newPasswordET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                oldPassword = oldPasswordET.getText().toString();
                newPassword = newPasswordET.getText().toString();

                if (oldPassword.equals("")) {
                    Toast.makeText(baseContext,"Please input old password", Toast.LENGTH_LONG).show();
                    oldPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    return ;
                }

                if (newPassword.equals("")) {
                    Toast.makeText(baseContext,"Please input new password", Toast.LENGTH_LONG).show();
                    newPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    return ;
                }

                if (oldPassword.equals(newPassword)) {
                    Toast.makeText(baseContext,"Old and new passwords are same", Toast.LENGTH_LONG).show();
                    newPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    oldPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                    return ;
                }

                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                waitIndicator.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.ResetPassword(oldPassword, newPassword);
                    }
                }).start();
            }
        });

        showPasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();

                if(passwordFlag) {
                    showPasswordB.setText(hidePassword);
                    newPasswordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPasswordET.setSelection(newPasswordET.getText().length());
                    passwordFlag = false;
                } else {
                    showPasswordB.setText(showPassword);
                    newPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPasswordET.setSelection(newPasswordET.getText().length());
                    passwordFlag = true;
                }
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void resetBackgroundColors() {
        oldPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        newPasswordET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }
}

