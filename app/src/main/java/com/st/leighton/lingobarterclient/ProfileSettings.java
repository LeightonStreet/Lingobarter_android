package com.st.leighton.lingobarterclient;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class ProfileSettings extends AppCompatActivity {

    final Integer baseLevel = 0;
    final Integer highestLevel = 5;
    private static final int SELECT_PICTURE = 1;

    Context baseContext;

    EditText fullnameET;
    EditText locationET;
    EditText birthdayET;
    EditText nativeLanguageET;
    EditText learnLanguageET;

    Spinner nationalityS;

    Button uploadAvatarB;
    Button submitB;
    Button cancelB;

    RadioButton maleRB;
    RadioButton femaleRB;

    ImageView avatarIV;
    ListView nativeLanguageLV;
    ListView learnLanguageLV;

    Calendar birthdayC;
    Geocoder geocoder;
    Locale[] locales;
    DatePickerDialog.OnDateSetListener dateSetListner;
    AlertDialog.Builder nativeLanguageAlertDialogBuilder;
    AlertDialog.Builder learnLanguageAlertDialogBuilder;

    boolean gender = true;
    String fullname="", birthday="", nation="", image_path="";
    double latitude, longitude, birthday_timestamp;

    String[] languages;
    ArrayList<String> countries;
    ArrayAdapter<String> countryAdapter, languageAdapter;

    HashSet<String> nativeLanguages;
    HashMap<String, Integer> learnLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        baseContext = this;

        fullnameET = (EditText) findViewById(R.id.hx_profile_settings_edit_name);
        locationET = (EditText) findViewById(R.id.hx_profile_settings_edit_location);
        birthdayET = (EditText) findViewById(R.id.hx_profile_settings_edit_birthday);
        nativeLanguageET = (EditText) findViewById(R.id.hx_profile_settings_edit_native_languages);
        learnLanguageET = (EditText) findViewById(R.id.hx_profile_settings_edit_learn_languages);

        nationalityS = (Spinner) findViewById(R.id.hx_profile_settings_spinner_nationality);

        uploadAvatarB = (Button) findViewById(R.id.hx_profile_settings_button_avatar);
        submitB = (Button) findViewById(R.id.hx_profile_settings_button_submit);

        maleRB = (RadioButton) findViewById(R.id.hx_profile_settings_gender_male);
        femaleRB = (RadioButton) findViewById(R.id.hx_profile_settings_gender_female);

        avatarIV = (ImageView) findViewById(R.id.hx_profile_settings_image_avatar);

        birthdayC = Calendar.getInstance();
        nativeLanguageLV = new ListView(this);
        learnLanguageLV = new ListView(this);

        geocoder = new Geocoder(this, Locale.getDefault());
        nativeLanguageAlertDialogBuilder = new AlertDialog.Builder(baseContext);
        learnLanguageAlertDialogBuilder = new AlertDialog.Builder(baseContext);

        nativeLanguages = new HashSet<>();
        learnLanguages = new HashMap<>();
        nativeLanguages.clear();
        learnLanguages.clear();
    }
}
