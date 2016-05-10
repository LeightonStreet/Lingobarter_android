package com.st.leighton.lingobarterclient;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelfInformation extends AppCompatActivity {

    Context baseContext;

    EditText taglineET;
    EditText biographyET;

    Button updateB;
    Button cancelB;

    String tagline, biography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_information);

        baseContext = this;

        taglineET = (EditText) findViewById(R.id.hx_self_information_edit_tagline);
        biographyET = (EditText) findViewById(R.id.hx_self_information_edit_biography);

        updateB = (Button) findViewById(R.id.hx_self_information_button_submit);
        cancelB = (Button) findViewById(R.id.hx_self_information_button_cancel);

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagline = taglineET.getText().toString();
                biography = biographyET.getText().toString();

                finish();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
