package com.st.leighton.lingobarterclient;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        Button registerBtn = (Button) findViewById(R.id.hx_login_button_register);
        Button retrieveBtn = (Button) findViewById(R.id.hx_login_button_forget_password);
        if (registerBtn != null) {
            registerBtn.setPaintFlags(registerBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (retrieveBtn != null) {
            retrieveBtn.setPaintFlags(retrieveBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void check_identity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
