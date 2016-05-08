package com.st.leighton.lingobarterclient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        baseContext = this;

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
                        Intent intent = new Intent(baseContext, BasicProfile.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("BACK TO LOGIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(baseContext, Login.class);
                        startActivity(intent);
                    }
                });

        Bundle extra = getIntent().getExtras();
        if(extra == null) {
            email = "";
        } else {
            email = extra.getString(Register.EMAIL_KEY);
        }

        emailTV.setText(email);

        resendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baseContext,"Email has been sent, please check your email account.", Toast.LENGTH_LONG).show();
            }
        });

        confirmedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog confirmAlertDialog = confirmAlertDialogBuilder.create();
                confirmAlertDialog.show();
            }
        });
    }
}
