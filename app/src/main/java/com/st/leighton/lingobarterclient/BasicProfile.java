package com.st.leighton.lingobarterclient;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class BasicProfile extends AppCompatActivity {
    Context baseContext;

    EditText fullnameET;
    EditText locationET;
    EditText birthdayET;
    EditText nativeLanguageET;
    EditText learnLanguageET;

    Spinner nationalityS;

    Button uploadAvatarB;
    Button submitB;

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
    String fullname="", birthday="", nation="";
    double latitude, longitude, birthday_timestamp;

    ArrayList<String> countries, languages;
    ArrayAdapter<String> countryAdapter, languageAdapter;

    HashSet<String> nativeLanguages;
    HashMap<String, Integer> learnLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        baseContext = this;

        fullnameET = (EditText) findViewById(R.id.hx_basic_profile_edit_name);
        locationET = (EditText) findViewById(R.id.hx_basic_profile_edit_location);
        birthdayET = (EditText) findViewById(R.id.hx_basic_profile_edit_birthday);
        nativeLanguageET = (EditText) findViewById(R.id.hx_basic_profile_edit_native_languages);
        learnLanguageET = (EditText) findViewById(R.id.hx_basic_profile_edit_learn_languages);

        nationalityS = (Spinner) findViewById(R.id.hx_basic_profile_spinner_nationality);

        uploadAvatarB = (Button) findViewById(R.id.hx_basic_profile_button_avatar);
        submitB = (Button) findViewById(R.id.hx_basic_profile_button_submit);

        maleRB = (RadioButton) findViewById(R.id.hx_basic_profile_gender_male);
        femaleRB = (RadioButton) findViewById(R.id.hx_basic_profile_gender_female);

        avatarIV = (ImageView) findViewById(R.id.hx_basic_profile_image_avatar);
        nativeLanguageLV = new ListView(this);
        learnLanguageLV = new ListView(this);

        birthdayC = Calendar.getInstance();

        geocoder = new Geocoder(this, Locale.getDefault());

        nativeLanguageAlertDialogBuilder = new AlertDialog.Builder(baseContext);
        learnLanguageAlertDialogBuilder = new AlertDialog.Builder(baseContext);

        nativeLanguages = new HashSet<>();
        learnLanguages = new HashMap<>();
        nativeLanguages.clear();
        learnLanguages.clear();

        fullnameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        locationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
            }
        });

        dateSetListner =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthdayC.set(Calendar.YEAR, year);
                birthdayC.set(Calendar.MONTH, monthOfYear);
                birthdayC.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String format = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

                birthday = dateFormat.format(birthdayC.getTime());
                birthday_timestamp = birthdayC.getTimeInMillis();
                birthdayET.setText(birthday);
            }
        };

        birthdayET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                new DatePickerDialog(BasicProfile.this, dateSetListner,
                        birthdayC.get(Calendar.YEAR),
                        birthdayC.get(Calendar.MONTH),
                        birthdayC.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        locales = Locale.getAvailableLocales();
        countries = new ArrayList<>();
        languages = new ArrayList<>();

        String country, language;
        for (Locale loc: locales) {
            country = loc.getDisplayCountry();
            language = loc.getDisplayLanguage();

            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
            if (language.length() > 0 && !languages.contains(language)) {
                languages.add(language);
            }
        }

        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(languages, String.CASE_INSENSITIVE_ORDER);
        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);

        nationalityS.setAdapter(countryAdapter);
        nationalityS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resetBackgroundColors();
                nation = countries.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        nativeLanguageLV.setItemChecked(2, true);
        nativeLanguageLV.setAdapter(languageAdapter);
        nativeLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        nativeLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choice = languages.get(position);

                if(nativeLanguages.contains(choice)) {
                    nativeLanguages.remove(choice);
                    parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.DimGray));
                } else {
                    nativeLanguages.add(choice);
                    parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.background_tab_pressed));
                }

                nativeLanguageET.setText(mergeNativeLanguage(nativeLanguages));
            }
        });

        nativeLanguageAlertDialogBuilder.setTitle("Native languages");
        nativeLanguageAlertDialogBuilder.setView(nativeLanguageLV);
        nativeLanguageAlertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(nativeLanguageLV.getParent()!=null) {
                    ((ViewGroup)nativeLanguageLV.getParent()).removeView(nativeLanguageLV);
                }
            }
        });
        nativeLanguageAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(nativeLanguageLV.getParent()!=null) {
                    ((ViewGroup)nativeLanguageLV.getParent()).removeView(nativeLanguageLV);
                }
            }
        });

        nativeLanguageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = nativeLanguageAlertDialogBuilder.create();
                dialog.show();

            }
        });

        ///////////////////////////////////

//        nativeLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        nativeLanguageLV.setItemChecked(2, true);
//        nativeLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String choice = languages.get(position);
//
//                if(nativeLanguages.contains(choice)) {
//                    nativeLanguages.remove(choice);
//                    Toast.makeText(baseContext, choice + " has been removed.", Toast.LENGTH_SHORT).show();
//                    parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.DimGray));
//                    ((TextView) view).setText("Changed!");
//                } else {
//                    nativeLanguages.add(choice);
//                    Toast.makeText(baseContext, choice + " has been selected.", Toast.LENGTH_SHORT).show();
//                    parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.background_tab_pressed));
//                }
//            }
//        });



//        final Dialog dialog = builder.create();
//
//        dialog.show();
//        nativeLanguageET.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ListView nativeLanguageLV = new ListView(baseContext);
//                languageAdapter = new ArrayAdapter<>(baseContext, android.R.layout.simple_spinner_item, languages);
//                nativeLanguageLV.setAdapter(languageAdapter);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(baseContext);
//                builder.setTitle("Native languages");
//                builder.setView(nativeLanguageLV);
//
//                final Dialog dialog = builder.create();
//                dialog.show();
//            }
//        });




        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();

                fullname = fullnameET.getText().toString();
                if(fullname.equals("")) {
                    Toast.makeText(baseContext,"Please specify your name.", Toast.LENGTH_LONG).show();
                    fullnameET.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    return ;
                }

                if(!maleRB.isChecked()) {
                    gender = false;
                }

                if(birthday.equals("")) {
                    Toast.makeText(baseContext,"Please select your birthday.", Toast.LENGTH_LONG).show();
                    birthdayET.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    return ;
                }

                if(nation.equals("")) {
                    Toast.makeText(baseContext,"Please select your nationality.", Toast.LENGTH_LONG).show();
                    nationalityS.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    return ;
                }

                String location = locationET.getText().toString();
                if(location.equals("")) {
                    Toast.makeText(baseContext,"Please specify your location.", Toast.LENGTH_LONG).show();
                    locationET.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    return ;
                } else {
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(location, 1);
                        Address address = addresses.get(0);
                        latitude = address.getLatitude();
                        longitude = address.getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        builder.setTitle("Native languages");
//
//        nativeLanguageLV = new ListView(this);
//        nativeLanguageLV.setAdapter(languageAdapter);
//        nativeLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        nativeLanguageLV.setItemChecked(2, true);
//        nativeLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String choice = languages.get(position);
//
//                if(nativeLanguages.contains(choice)) {
//                    nativeLanguages.remove(choice);
//                    Toast.makeText(baseContext, choice + " has been removed.", Toast.LENGTH_SHORT).show();
//                    parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.DimGray));
////                    ((TextView) view).setText("Changed!");
//                } else {
//                    nativeLanguages.add(choice);
//                    Toast.makeText(baseContext, choice + " has been selected.", Toast.LENGTH_SHORT).show();
//                    parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.background_tab_pressed));
//                }
//            }
//        });
//
//        builder.setView(nativeLanguageLV);
//
//
//        nativeLanguageET = (EditText) findViewById(R.id.hx_basic_profile_edit_native_languages);
//        nativeLanguageET.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = builder.create();
//                dialog.show();
//            }
//        });
    }

    void resetBackgroundColors() {
        fullnameET.setBackgroundColor(getResources().getColor(R.color.DimGray));
        birthdayET.setBackgroundColor(getResources().getColor(R.color.DimGray));
        locationET.setBackgroundColor(getResources().getColor(R.color.DimGray));
        nativeLanguageET.setBackgroundColor(getResources().getColor(R.color.DimGray));
        learnLanguageET.setBackgroundColor(getResources().getColor(R.color.DimGray));

        nationalityS.setBackgroundColor(getResources().getColor(R.color.DimGray));

        uploadAvatarB.setBackgroundColor(getResources().getColor(R.color.DimGray));
    }

    String mergeNativeLanguage(HashSet<String> nativeLanguages) {
        String rtn = "";
        for(String item : nativeLanguages) {
            rtn += item + ", ";
        }
        if (rtn.length() > 0 && rtn.charAt(rtn.length() - 1)==',') {
            rtn = rtn.substring(0, rtn.length()-1);
        }

        return rtn;
    }
}
