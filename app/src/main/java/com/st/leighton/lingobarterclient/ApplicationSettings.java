package com.st.leighton.lingobarterclient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashSet;

public class ApplicationSettings extends AppCompatActivity {

    Context baseContext;

    RadioButton strictMatchRB;
    RadioButton sameGenderRB;
    RadioButton hideNearbyRB;
    RadioButton noHideNearbyRB;
    RadioButton hideSearchRB;
    RadioButton noHideSearchRB;
    RadioButton confirmationRB;

    EditText hideInfoET;
    EditText fromAgeET;
    EditText toAgeET;

    Button updateB;
    Button cancelB;

    String[] infoFields;
    ArrayAdapter<String> infoFieldAdapter;
    ListView infoFieldLV;
    AlertDialog.Builder infoFieldAlertDialog;

    int fromAge, toAge;
    HashSet<String> hideInfoFields;
    boolean strictFlag, sameGenderFlag, nearbyFlag, searchFlag, confirmFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);

        baseContext = this;

        strictMatchRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_strict_yes);
        sameGenderRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_gender_yes);
        hideNearbyRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_nearby_yes);
        noHideNearbyRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_nearby_no);
        hideSearchRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_search_yes);
        noHideSearchRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_search_no);
        confirmationRB = (RadioButton) findViewById(R.id.hx_application_settings_radio_confirm_yes);

        hideInfoET = (EditText) findViewById(R.id.hx_application_settings_edit_info);
        fromAgeET = (EditText) findViewById(R.id.hx_application_settings_edit_age_from);
        toAgeET = (EditText) findViewById(R.id.hx_application_settings_edit_age_to);

        updateB = (Button) findViewById(R.id.hx_application_settings_button_submit);
        cancelB = (Button) findViewById(R.id.hx_application_settings_button_cancel);

        hideSearchRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hideNearbyRB.setChecked(true);
                    noHideNearbyRB.setChecked(false);
                }
            }
        });

        noHideNearbyRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && hideSearchRB.isChecked()) {
                    hideSearchRB.setChecked(false);
                    noHideSearchRB.setChecked(true);
                }
            }
        });

        hideInfoFields = new HashSet<>();
        hideInfoFields.clear();

        infoFields = getResources().getStringArray(R.array.info_array);
        infoFieldAdapter = new ArrayAdapter<>(baseContext, android.R.layout.simple_list_item_1, infoFields);

        infoFieldLV = new ListView(baseContext);
        infoFieldLV.setAdapter(infoFieldAdapter);
        infoFieldLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        infoFieldLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String choice = infoFields[(int) id];

                if(hideInfoFields.contains(choice)) {
                    hideInfoFields.remove(choice);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
                } else {
                    hideInfoFields.add(choice);
                    view.setBackgroundColor(ContextCompat.getColor(baseContext,R.color.background_tab_pressed));
                }

                hideInfoET.setText(mergeInfoFields(hideInfoFields));
            }
        });

        infoFieldAlertDialog = new AlertDialog.Builder(baseContext);
        infoFieldAlertDialog.setTitle("Information Field");
        infoFieldAlertDialog.setView(infoFieldLV);
        infoFieldAlertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(infoFieldLV.getParent() != null) {
                    ((ViewGroup) infoFieldLV.getParent()).removeView(infoFieldLV);
                }
            }
        });

        hideInfoET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackgroundColors();
                final Dialog dialog = infoFieldAlertDialog.create();
                dialog.show();
            }
        });

        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strictFlag = strictMatchRB.isChecked();
                sameGenderFlag = sameGenderRB.isChecked();
                nearbyFlag = hideNearbyRB.isChecked();
                searchFlag = hideSearchRB.isChecked();
                confirmFlag = confirmationRB.isChecked();

                if (!fromAgeET.getText().toString().matches("") && !toAgeET.getText().toString().matches("")) {
                    fromAge = Integer.parseInt(fromAgeET.getText().toString());
                    toAge = Integer.parseInt(toAgeET.getText().toString());

                    if (fromAge > toAge) {
                        Toast.makeText(baseContext, "Please specify valid age range.", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(baseContext, "Please specify age range.", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(baseContext,"Succeed", Toast.LENGTH_LONG).show();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void resetBackgroundColors() {
        fromAgeET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
        toAgeET.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DimGray));
    }

    String mergeInfoFields(HashSet<String> hideInfoFields) {
        String rtn = "";
        for(String item : hideInfoFields) {
            rtn += item + ", ";
        }
        if (rtn.length() > 0) {
            rtn = rtn.substring(0, rtn.length() - 2);
        }

        return rtn;
    }
}
