package com.st.leighton.lingobarterclient;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BasicProfile extends AppCompatActivity {
    EditText birthday;
    Calendar calendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        Spinner learnLangSpinner = (Spinner) findViewById(R.id.hx_basic_profile_spinner_learn);
        Spinner teachLangSpinner = (Spinner) findViewById(R.id.hx_basic_profile_spinner_teach);
        Spinner nationalitySpinner = (Spinner) findViewById(R.id.hx_basic_profile_spinner_nationality);

        ArrayAdapter<CharSequence> langAdapter = ArrayAdapter.createFromResource(
                this, R.array.language_array, android.R.layout.simple_spinner_item
        );
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> nationalityAdapter = ArrayAdapter.createFromResource(
                this, R.array.nationality_array, android.R.layout.simple_spinner_item
        );
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(learnLangSpinner != null && teachLangSpinner != null && nationalitySpinner != null) {
            learnLangSpinner.setAdapter(langAdapter);
            teachLangSpinner.setAdapter(langAdapter);
            nationalitySpinner.setAdapter(nationalityAdapter);

            learnLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            teachLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        birthday = (EditText) findViewById(R.id.hx_basic_profile_edit_birthday);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BasicProfile.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    private void updateLabel() {
        String format = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        birthday.setText(dateFormat.format(calendar.getTime()));
    }
}
