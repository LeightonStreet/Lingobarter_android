package com.st.leighton.lingobarterclient;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class ForgetPassword extends AppCompatActivity {

    Button loginB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        loginB = (Button) findViewById(R.id.hx_forget_button_login);
        if (loginB != null) {
            loginB.setPaintFlags(loginB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
