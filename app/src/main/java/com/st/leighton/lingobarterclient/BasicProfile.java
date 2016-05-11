package com.st.leighton.lingobarterclient;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BasicProfile extends AppCompatActivity {
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

    Uri selectedImageUri = null;
    boolean gender = true;
    String fullname="", birthday="", nation="";
    double latitude, longitude;
    long birthday_timestamp;

    String[] languages;
    ArrayList<String> countries;
    ArrayAdapter<String> countryAdapter, languageAdapter;

    HashSet<String> nativeLanguages;
    HashMap<String, Integer> learnLanguages;

    Webservice socketService;
    BroadcastReceiver noticeReceiver, imageNoticeReceiver;

    ProgressDialog waitIndicator;
    final public static String PROFILE_FEEDBACK = "PROFILE_FEEDBACK";
    final public static String PROFILE_IMAGE_FEEDBACK = "PROFILE_IMAGE_FEEDBACK";

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.BasicProfile");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (waitIndicator.isShowing()) {
                    waitIndicator.cancel();
                }
                String message = intent.getStringExtra(PROFILE_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Intent main_intent = new Intent(baseContext, MainActivity.class);
                        startActivity(main_intent);
                        finish();
                        break;

                    case "ERROR":
                        Toast.makeText(baseContext,"Cannot connect to server, please check your network", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }
        };
        this.registerReceiver(noticeReceiver, noticeFilter);

        IntentFilter imageNoticeFilter = new IntentFilter("android.intent.action.BasicProfileImage");
        imageNoticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(PROFILE_IMAGE_FEEDBACK);

                switch (message) {
                    case "Succeed":
                        Toast.makeText(baseContext,"Avatar has been uploaded", Toast.LENGTH_LONG).show();
                        break;

                    case "ERROR":
                        Toast.makeText(baseContext,"Cannot connect to server, please check your network", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }
        };
        this.registerReceiver(imageNoticeReceiver, imageNoticeFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.noticeReceiver);
        this.unregisterReceiver(this.imageNoticeReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        baseContext = this;

        socketService = Webservice.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

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
                birthday_timestamp = birthdayC.getTimeInMillis()/1000;
                Log.d("Birthday", Long.toString(birthday_timestamp));
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
        languages = getResources().getStringArray(R.array.language_array);

        for (Locale loc: locales) {
            String country = loc.getDisplayCountry();

            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);

        nationalityS.setAdapter(countryAdapter);
        nationalityS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resetBackgroundColors();
                nation = countries.get((int)id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        nativeLanguageLV.setAdapter(languageAdapter);
        nativeLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        nativeLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choice = languages[(int)id];

                if(nativeLanguages.contains(choice)) {
                    nativeLanguages.remove(choice);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
                } else {
                    nativeLanguages.add(choice);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext,R.color.background_tab_pressed));
                }

                nativeLanguageET.setText(mergeNativeLanguage(nativeLanguages));
            }
        });

        nativeLanguageAlertDialogBuilder.setTitle("Native languages");
        nativeLanguageAlertDialogBuilder.setView(nativeLanguageLV);
        nativeLanguageAlertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
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
                resetBackgroundColors();
                final Dialog dialog = nativeLanguageAlertDialogBuilder.create();
                dialog.show();
            }
        });

        learnLanguageLV.setAdapter(languageAdapter);
        learnLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        learnLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choice = languages[(int)id];

                if(learnLanguages.containsKey(choice)) {
                    Integer level = learnLanguages.get(choice);

                    if (level.equals(highestLevel)) {
                        learnLanguages.remove(choice);
                        view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
                        ((TextView)view).setText(choice);
                    } else {
                        level += 1;
                        learnLanguages.put(choice, level);

                        String entry_text = choice + " (" + getDescriptionByLevel(level) + ")";
                        ((TextView)view).setText(entry_text);
                    }

                } else {
                    learnLanguages.put(choice, baseLevel);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.background_tab_pressed));

                    String entry_text = choice + " (" + getDescriptionByLevel(baseLevel) + ")";
                    ((TextView)view).setText(entry_text);
                }

                learnLanguageET.setText(mergeLearnLanguage(learnLanguages));
            }
        });

        learnLanguageAlertDialogBuilder.setTitle("Learn languages");
        learnLanguageAlertDialogBuilder.setView(learnLanguageLV);
        learnLanguageAlertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(learnLanguageLV.getParent()!=null) {
                    ((ViewGroup)learnLanguageLV.getParent()).removeView(learnLanguageLV);
                }
            }
        });

        learnLanguageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                final Dialog dialog = learnLanguageAlertDialogBuilder.create();
                dialog.show();
            }
        });

        uploadAvatarB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select picture"), SELECT_PICTURE);
            }
        });

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();

                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                waitIndicator.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fullname = fullnameET.getText().toString();
                        if(fullname.equals("")) {
                            Toast.makeText(baseContext,"Please specify your name.", Toast.LENGTH_LONG).show();
                            fullnameET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        }

                        if(!maleRB.isChecked()) {
                            gender = false;
                        }

                        if(birthday.equals("")) {
                            Toast.makeText(baseContext,"Please select your birthday.", Toast.LENGTH_LONG).show();
                            birthdayET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        }

                        if(nation.equals("")) {
                            Toast.makeText(baseContext,"Please select your nationality.", Toast.LENGTH_LONG).show();
                            nationalityS.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        }

                        String location = locationET.getText().toString();
                        if(location.equals("")) {
                            Toast.makeText(baseContext,"Please specify your location.", Toast.LENGTH_LONG).show();
                            locationET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        } else {
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                                if (addresses.isEmpty()) {
                                    Toast.makeText(baseContext,"City name is not valid.", Toast.LENGTH_LONG).show();
                                    locationET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                                    return ;
                                }
                                Address address = addresses.get(0);
                                latitude = address.getLatitude();
                                longitude = address.getLongitude();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if(nativeLanguages.isEmpty()) {
                            Toast.makeText(baseContext,"Please specify your native languages.", Toast.LENGTH_LONG).show();
                            nativeLanguageET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        }

                        if(learnLanguages.isEmpty()) {
                            Toast.makeText(baseContext,"Please specify languages you want to learn.", Toast.LENGTH_LONG).show();
                            learnLanguageET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        }

                        if(selectedImageUri == null) {
                            Toast.makeText(baseContext,"Please upload an avatar.", Toast.LENGTH_LONG).show();
                            uploadAvatarB.setTextColor(ContextCompat.getColor(baseContext, R.color.colorAccent));
                            return ;
                        }

                        if (noOverlapLanguages(nativeLanguages, learnLanguages)) {
                                socketService.BasicSetBasicProfile(fullname, gender, nation,
                                        latitude, longitude, birthday_timestamp,
                                        nativeLanguages, learnLanguages);
                        } else {
                            Toast.makeText(baseContext,"Overlapped language choices", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                avatarIV.setImageURI(selectedImageUri);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.BasicUploadAvatar(getPath(selectedImageUri));
                    }
                }).start();
            }
        }
    }

    void resetBackgroundColors() {
        fullnameET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        birthdayET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        locationET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        nativeLanguageET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        learnLanguageET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));

        nationalityS.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));

        uploadAvatarB.setTextColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }

    String mergeNativeLanguage(HashSet<String> nativeLanguages) {
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

    Boolean noOverlapLanguages(HashSet<String> nativeLanguages, HashMap<String, Integer> learnLanguages) {
        for(String item : nativeLanguages) {
            if(learnLanguages.containsKey(item)) {
                return false;
            }
        }
        return true;
    }
}
