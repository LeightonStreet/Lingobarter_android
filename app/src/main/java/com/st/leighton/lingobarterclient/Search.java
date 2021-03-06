package com.st.leighton.lingobarterclient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Search extends AppCompatActivity {

    Context baseContext;

    RadioButton allGenderRB;
    RadioButton maleRB;
    RadioButton femaleRB;

    EditText fromAgeET;
    EditText toAgeET;
    EditText nationalityET;
    EditText teachLanguageET;
    EditText learnLanguageET;

    Button luckyB;
    Button searchB;
    Button returnB;

    ListView nationalityLV;
    ListView teachLanguageLV;
    ListView learnLanguageLV;

    final private Integer baseLevel = 0;
    final private Integer highestLevel = 5;

    Geocoder geocoder;
    Locale[] locales;
    AlertDialog.Builder nationalityAlertDialogBuilder;
    AlertDialog.Builder teachLanguageAlertDialogBuilder;
    AlertDialog.Builder learnLanguageAlertDialogBuilder;

    String[] languages;
    ArrayList<String> countries;
    ArrayAdapter<String> countryAdapter, languageAdapter;

    boolean maleFlag, femaleFlag, allGenderFlag;
    Integer fromAge, toAge;
    HashSet<String> nationalities;
    HashMap<String, Integer> teachLanguages, learnLanguages;

    final static public String NAME_KEY = "SEARCH_NAME_KEY";
    final static public String USERNAME_KEY = "SEARCH_USERNAME_KEY";
    final static public String IMAGEURL_KEY = "SEARCH_IMAGEURL_KEY";
    final static public String GENDER_KEY = "SEARCH_GENDER_KEY";
    final static public String BIRTHDAY_KEY = "SEARCH_BIRTHDAY_KEY";
    final static public String TAGLINE_KEY = "SEARCH_TAGLINE_KEY";
    final static public String BIOGRAPHY_KEY = "SEARCH_BIOGRAPHY_KEY";
    final static public String CITY_KEY = "SEARCH_CITY_KEY";
    final static public String NATIONALITY_KEY = "SEARCH_NATIONALITY_KEY";
    final static public String LEARN_LANGUAGES_KEY = "SEARCH_LEARN_LANGUAGES_KEY";
    final static public String TEACH_LANGUAGES_KEY = "SEARCH_TEACH_LANGUAGES_KEY";

    Webservice socketService;
    BroadcastReceiver noticeReceiver;

    ProgressDialog waitIndicator;
    final public static String USER_PROFILES_BUNDLE_KEY = "USER_PROFILES_BUNDLE_KEY";

//    private void test() {
//        final String[] usernames;
//        ArrayAdapter<String> usernameAdapter;
//
//        final ListView usernamesLV;
//        AlertDialog.Builder usersAlertDialog;
//
//        usernames = getResources().getStringArray(R.array.test_user);
//        usernameAdapter = new ArrayAdapter<>(baseContext, android.R.layout.simple_list_item_1,usernames);
//
//        usernamesLV = new ListView(baseContext);
//        usernamesLV.setAdapter(usernameAdapter);
//        usernamesLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        usernamesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String name = "Haoxiang Xu";
//                String username = usernames[(int) id];
//
//                String imageURL = "Default URL";
//                String gender = "Male";
//                String birthday = "1993/2/6";
//                String tagline = "I'm super COOL!";
//                String biography = "I really want to meet someone here, please teach my English~";
//                String city = "Tengzhou";
//                String nationality = "China";
//
//                HashSet<String> teach_languages = new HashSet<>();
//                teach_languages.add("Chinese");
//                teach_languages.add("Cantonese");
//
//                HashMap<String, Integer> learn_languages = new HashMap<>();
//                learn_languages.put("English", 3);
//                learn_languages.put("Japanese", 1);
//
//                Bundle passToUserProfile = new Bundle();
//                passToUserProfile.putString(NAME_KEY, name);
//                passToUserProfile.putString(USERNAME_KEY, username);
//                passToUserProfile.putString(IMAGEURL_KEY, imageURL);
//                passToUserProfile.putString(GENDER_KEY, gender);
//                passToUserProfile.putString(BIRTHDAY_KEY, birthday);
//                passToUserProfile.putString(TAGLINE_KEY, tagline);
//                passToUserProfile.putString(BIOGRAPHY_KEY, biography);
//                passToUserProfile.putString(CITY_KEY, city);
//                passToUserProfile.putString(NATIONALITY_KEY, nationality);
//                passToUserProfile.putSerializable(LEARN_LANGUAGES_KEY, learn_languages);
//                passToUserProfile.putSerializable(TEACH_LANGUAGES_KEY, teach_languages);
//
//                Intent intent = new Intent(baseContext, UserProfile.class);
//                intent.putExtras(passToUserProfile);
//                startActivity(intent);
//            }
//        });
//
//        usersAlertDialog = new AlertDialog.Builder(baseContext);
//        usersAlertDialog.setTitle("Users");
//        usersAlertDialog.setView(usernamesLV);
//        usersAlertDialog.setPositiveButton("RETURN", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(usernamesLV.getParent() != null) {
//                    ((ViewGroup) usernamesLV.getParent()).removeView(usernamesLV);
//                }
//            }
//        });
//
//        final Dialog dialog = usersAlertDialog.create();
//        dialog.show();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter noticeFilter = new IntentFilter("android.intent.action.Search");
        noticeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                final HashMap<String, UserInfoBundle> userProfiles
                        = (HashMap<String, UserInfoBundle>) bundle.getSerializable(USER_PROFILES_BUNDLE_KEY);

                if (userProfiles.size() == 0) {
                    Toast.makeText(baseContext,"Cannot connect to server, please check your network", Toast.LENGTH_LONG).show();
                    return ;
                }

                final ArrayList<String> userNames = new ArrayList<>();
                for (String userName : userProfiles.keySet()) {
                    userNames.add(userName);
                }

                final ListView usernamesLV;
                AlertDialog.Builder usersAlertDialog;
                ArrayAdapter<String> userNamesAdapter
                        = new ArrayAdapter<>(baseContext, android.R.layout.simple_spinner_item, userNames);

                usernamesLV = new ListView(baseContext);
                usernamesLV.setAdapter(userNamesAdapter);
                usernamesLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                usernamesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String user_name = userNames.get((int) id);
                        UserInfoBundle user_info = userProfiles.get(user_name);

                        Bundle passToUserProfile = new Bundle();
                        passToUserProfile.putString(NAME_KEY, user_info.name);
                        passToUserProfile.putString(USERNAME_KEY, user_info.username);
                        passToUserProfile.putString(IMAGEURL_KEY, user_info.imageURL);
                        passToUserProfile.putString(GENDER_KEY, user_info.gender);
                        passToUserProfile.putString(BIRTHDAY_KEY, user_info.birthday);
                        passToUserProfile.putString(TAGLINE_KEY, user_info.tagline);
                        passToUserProfile.putString(BIOGRAPHY_KEY, user_info.biography);
                        passToUserProfile.putString(CITY_KEY, user_info.city);
                        passToUserProfile.putString(NATIONALITY_KEY, user_info.nationality);
                        passToUserProfile.putSerializable(LEARN_LANGUAGES_KEY, user_info.learn_languages);
                        passToUserProfile.putSerializable(TEACH_LANGUAGES_KEY, user_info.teach_languages);

                        Intent intent = new Intent(baseContext, UserProfile.class);
                        intent.putExtras(passToUserProfile);
                        startActivity(intent);
                    }
                });

                usersAlertDialog = new AlertDialog.Builder(baseContext);
                usersAlertDialog.setTitle("Users");
                usersAlertDialog.setView(usernamesLV);
                usersAlertDialog.setPositiveButton("RETURN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(usernamesLV.getParent() != null) {
                            ((ViewGroup) usernamesLV.getParent()).removeView(usernamesLV);
                        }
                    }
                });

                final Dialog dialog = usersAlertDialog.create();
                dialog.show();

                waitIndicator.cancel();
            }
        };
        this.registerReceiver(noticeReceiver, noticeFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.noticeReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        baseContext = this;

        socketService = Webservice.getInstance();
        waitIndicator = new ProgressDialog(baseContext);

        allGenderRB = (RadioButton) findViewById(R.id.hx_search_radio_all);
        maleRB = (RadioButton) findViewById(R.id.hx_search_radio_male);
        femaleRB = (RadioButton) findViewById(R.id.hx_search_radio_female);

        fromAgeET = (EditText) findViewById(R.id.hx_search_edit_age_from);
        toAgeET = (EditText) findViewById(R.id.hx_search_edit_age_to);
        nationalityET = (EditText) findViewById(R.id.hx_search_edit_nationality);
        teachLanguageET = (EditText) findViewById(R.id.hx_search_edit_teach_language);
        learnLanguageET = (EditText) findViewById(R.id.hx_search_edit_learn_language);

        luckyB = (Button) findViewById(R.id.hx_search_button_lucky);
        searchB = (Button) findViewById(R.id.hx_search_button_submit);
        returnB = (Button) findViewById(R.id.hx_search_button_cancel);

        nationalityLV = new ListView(baseContext);
        teachLanguageLV = new ListView(baseContext);
        learnLanguageLV = new ListView(baseContext);

        countries = new ArrayList<>();
        languages = getResources().getStringArray(R.array.language_array);

        geocoder = new Geocoder(baseContext, Locale.getDefault());
        locales = Locale.getAvailableLocales();

        for (Locale loc: locales) {
            String country = loc.getDisplayCountry();

            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        countryAdapter = new ArrayAdapter<>(baseContext, android.R.layout.simple_spinner_item, countries);
        languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);

        nationalityAlertDialogBuilder = new AlertDialog.Builder(baseContext);
        teachLanguageAlertDialogBuilder = new AlertDialog.Builder(baseContext);
        learnLanguageAlertDialogBuilder = new AlertDialog.Builder(baseContext);

        nationalities = new HashSet<>();
        teachLanguages = new HashMap<>();
        learnLanguages = new HashMap<>();

        nationalities.clear();
        teachLanguages.clear();
        learnLanguages.clear();

        nationalityLV.setAdapter(countryAdapter);
        nationalityLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        nationalityLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choice = countries.get((int)id);

                if (nationalities.contains(choice)) {
                    nationalities.remove(choice);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
                } else {
                    nationalities.add(choice);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext,R.color.background_tab_pressed));
                }

                nationalityET.setText(mergeNationalities(nationalities));
            }
        });

        nationalityAlertDialogBuilder.setTitle("Nationalities");
        nationalityAlertDialogBuilder.setView(nationalityLV);
        nationalityAlertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nationalityLV.getParent() != null) {
                    ((ViewGroup) nationalityLV.getParent()).removeView(nationalityLV);
                }
            }
        });

        nationalityET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = nationalityAlertDialogBuilder.create();
                dialog.show();
            }
        });

        teachLanguageLV.setAdapter(languageAdapter);
        learnLanguageLV.setAdapter(languageAdapter);

        teachLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        learnLanguageLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        teachLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choice = languages[(int)id];

                if(teachLanguages.containsKey(choice)) {
                    Integer level = teachLanguages.get(choice);

                    if (level.equals(highestLevel)) {
                        teachLanguages.remove(choice);
                        view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
                        ((TextView)view).setText(choice);
                    } else {
                        level += 1;
                        teachLanguages.put(choice, level);

                        String entry_text = choice + " (" + getDescriptionByLevel(level) + ")";
                        ((TextView)view).setText(entry_text);
                    }

                } else {
                    teachLanguages.put(choice, baseLevel);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.background_tab_pressed));

                    String entry_text = choice + " (" + getDescriptionByLevel(baseLevel) + ")";
                    ((TextView)view).setText(entry_text);
                }

                teachLanguageET.setText(mergeLanguages(teachLanguages));
            }
        });

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

                learnLanguageET.setText(mergeLanguages(learnLanguages));
            }
        });

        teachLanguageAlertDialogBuilder.setTitle("Teach languages");
        teachLanguageAlertDialogBuilder.setView(teachLanguageLV);
        teachLanguageAlertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(teachLanguageLV.getParent()!=null) {
                    ((ViewGroup)teachLanguageLV.getParent()).removeView(teachLanguageLV);
                }
            }
        });

        teachLanguageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = teachLanguageAlertDialogBuilder.create();
                dialog.show();
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
                final Dialog dialog = learnLanguageAlertDialogBuilder.create();
                dialog.show();
            }
        });

        luckyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baseContext,"Lucky dog!", Toast.LENGTH_LONG).show();
            }
        });

        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allGenderFlag = allGenderRB.isChecked();
                maleFlag = maleRB.isChecked();
                femaleFlag = femaleRB.isChecked();

                if (!fromAgeET.getText().toString().matches("") && !toAgeET.getText().toString().matches("")) {
                    fromAge = Integer.parseInt(fromAgeET.getText().toString());
                    toAge = Integer.parseInt(toAgeET.getText().toString());

                    if (fromAge > toAge) {
                        Toast.makeText(baseContext,"Please specify valid age range.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                waitIndicator.setMessage("Please wait...");
                waitIndicator.setCancelable(false);
                waitIndicator.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketService.Search(fromAge, toAge, teachLanguages, learnLanguages, nationalities);
                    }
                }).start();
            }
        });

        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    String mergeLanguages(HashMap<String, Integer> languages) {
        String rtn = "";
        Iterator it = languages.entrySet().iterator();
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

    String mergeNationalities(HashSet<String> nationalities) {
        String rtn = "";
        for(String item : nationalities) {
            rtn += item + ", ";
        }
        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 2);
        }

        return rtn;
    }
}
