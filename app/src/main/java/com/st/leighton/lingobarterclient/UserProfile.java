package com.st.leighton.lingobarterclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    Context baseContext;

    String imageURL;

    String name;
    String username;

    String gender;
    String birthday;

    String tagline;
    String biography;

    String city;
    String nationality;

    HashSet<String> teach_languages = null;
    HashMap<String, Integer> learn_languages = null;

    TextView mergedNameTV;
    TextView birthdayTV;
    TextView cityTV;
    TextView genderTV;
    TextView nationalityTV;
    TextView taglineTV;

    TextView teachLanguageTV;
    TextView learnLanguageTV;
    TextView biographyTV;

    Button sendRequestB;
    Button backB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        baseContext = this;

        mergedNameTV = (TextView) findViewById(R.id.hx_user_profile_content_merged_name);
        birthdayTV = (TextView) findViewById(R.id.hx_user_profile_content_birthday);
        cityTV = (TextView) findViewById(R.id.hx_user_profile_content_location);
        genderTV = (TextView) findViewById(R.id.hx_user_profile_content_gender);
        nationalityTV = (TextView) findViewById(R.id.hx_user_profile_content_nationality);
        taglineTV = (TextView) findViewById(R.id.hx_user_profile_content_tagline);
        teachLanguageTV = (TextView) findViewById(R.id.hx_user_profile_content_teach_languages);
        learnLanguageTV = (TextView) findViewById(R.id.hx_user_profile_content_learn_languages);
        biographyTV = (TextView) findViewById(R.id.hx_user_profile_content_biography);

        sendRequestB = (Button) findViewById(R.id.hx_user_profile_button_add_friend);
        backB = (Button) findViewById(R.id.hx_user_profile_button_back);
        backB.setPaintFlags(backB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        name = bundle.getString(Search.NAME_KEY);
        username = bundle.getString(Search.USERNAME_KEY);
        birthday = bundle.getString(Search.BIRTHDAY_KEY);
        city = bundle.getString(Search.CITY_KEY);
        gender = bundle.getString(Search.GENDER_KEY);
        nationality = bundle.getString(Search.NATIONALITY_KEY);
        tagline = bundle.getString(Search.TAGLINE_KEY);
        biography = bundle.getString(Search.BIOGRAPHY_KEY);
        teach_languages = (HashSet<String>) bundle.getSerializable(Search.TEACH_LANGUAGES_KEY);
        learn_languages = (HashMap<String, Integer>) bundle.getSerializable(Search.LEARN_LANGUAGES_KEY);

        String mergedName = name + " (" + username + ")";
        mergedNameTV.setText(mergedName);
        birthdayTV.setText(birthday);
        cityTV.setText(city);
        genderTV.setText(gender);
        nationalityTV.setText(nationality);
        taglineTV.setText(tagline);

        teachLanguageTV.setText(mergeNativeLanguage(teach_languages));
        learnLanguageTV.setText(mergeLearnLanguage(learn_languages));
        biographyTV.setText(biography);

        sendRequestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baseContext,"Send request succeed", Toast.LENGTH_LONG).show();
            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String mergeNativeLanguage(HashSet<String> nativeLanguages) {
        if (nativeLanguages == null) return "";

        String rtn = "";
        for(String item : nativeLanguages) {
            rtn += item + ", ";
        }
        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 2);
        }

        return rtn;
    }

    String getDescriptionByLevel(Integer level) {
        switch (level) {
            case 0: return "Beginner";
            case 1: return "Elementary";
            case 2: return "Intermediate";
            case 3: return "Advanced";
            case 4: return "Proficient";
            case 5: return "Native";
        }
        return "";
    }

    String mergeLearnLanguage(HashMap<String, Integer> learnLanguages) {
        if (learnLanguages == null) return "";

        String rtn = "";
        Iterator it = learnLanguages.entrySet().iterator();
        while(true) {
            if(!it.hasNext()) break;
            Map.Entry pair = (Map.Entry) it.next();
            rtn += pair.getKey() + " (" + getDescriptionByLevel((Integer) pair.getValue()) + "), ";
        }

        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 2);
        }
        return rtn;
    }
}
