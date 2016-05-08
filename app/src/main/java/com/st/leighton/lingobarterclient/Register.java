package com.st.leighton.lingobarterclient;

import android.content.Context;
import android.content.Intent;
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
    final private String hidePassword = "HIDE PASSWORD", showPassword = "SHOW PASSWORD";
    String username = "", email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        baseContext = this;

        registerB = (Button) findViewById(R.id.hx_register_button_register);
        showPasswordB = (Button) findViewById(R.id.hx_register_button_show_password);
        loginB = (Button) findViewById(R.id.hx_register_button_login);

        usernameET = (EditText) findViewById(R.id.hx_register_edit_user_name);
        emailET = (EditText) findViewById(R.id.hx_register_edit_email);
        passwordET = (EditText) findViewById(R.id.hx_register_edit_password);

        loginB.setPaintFlags(loginB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        showPasswordB.setPaintFlags(showPasswordB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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

                Toast.makeText(baseContext,"Succeed.", Toast.LENGTH_LONG).show();
            }
        });

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, Login.class);
                startActivity(intent);
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

    boolean isEmailValid(String email) {
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
