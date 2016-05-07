package com.st.leighton.lingobarterclient;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button loginBtn = (Button) findViewById(R.id.hx_register_button_login);
        Button showPasswordBtn = (Button) findViewById(R.id.hx_register_button_show_password);
        if (loginBtn != null) {
            loginBtn.setPaintFlags(loginBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (showPasswordBtn != null) {
            showPasswordBtn.setPaintFlags(showPasswordBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.finish();
    }

    public void submit_info_first(View view) {
        Intent intent = new Intent(this, BasicProfile.class);
        startActivity(intent);
        this.finish();
    }
}
