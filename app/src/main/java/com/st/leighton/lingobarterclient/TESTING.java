package com.st.leighton.lingobarterclient;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TESTING extends AppCompatActivity {

    Context baseContext;

    Button selfProfileB;
    Button selfIntroductionB;
    Button applicationSettingsB;

    Button searchB;

    Webservice socketService;
    BroadcastReceiver noticeReceiver;

    ProgressDialog waitIndicator;
    final public static String SELF_INFO_KEY = "SELF_INFO_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);



        baseContext = this;

        selfProfileB = (Button) findViewById(R.id.TESTING_SELF_PROFILE);
        selfIntroductionB = (Button) findViewById(R.id.TESTING_SELF_INTRODUCTION);
        applicationSettingsB = (Button) findViewById(R.id.TESTING_APPLICATION_SETTING);

        searchB = (Button) findViewById(R.id.TESTING_SEARCH);

        selfProfileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, ProfileSettings.class);
                startActivity(intent);
            }
        });

        selfIntroductionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, SelfInformation.class);
                startActivity(intent);
            }
        });

        applicationSettingsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, ApplicationSettings.class);
                startActivity(intent);
            }
        });

        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, Search.class);
                startActivity(intent);
            }
        });
    }
}
