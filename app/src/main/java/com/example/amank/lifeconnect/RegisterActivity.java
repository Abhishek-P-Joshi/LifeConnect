package com.example.amank.lifeconnect;

/**
 * Created by Yogesh on 11/20/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterActivity extends LoginActivity implements AdapterView.OnItemSelectedListener
{
    String strName, strEmail, strDOB, strContact, strPassword, strGender, strRole, strSelectedDr;
    String sql=null;

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private RadioGroup radioRoleGroup;
    private RadioButton radioRoleButton;
    private EditText etName;
    private EditText etEmail;
    private EditText etContact;
    private EditText etPassword;
    private Button btnSignUp;
    private Spinner spinDrList;
    private LinearLayout spinnerDrSelect;

    private Calendar calendar;
    private int year, month, day;
    private TextView dateView;

    ArrayList<String> DoctorList = new ArrayList<String>();
    private String JsonString;
    JSONArray doctors = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dateView = (TextView) findViewById(R.id.DOB);
        etName = (EditText)findViewById(R.id.editName);
        etEmail = (EditText)findViewById(R.id.editEmailId);
        etContact = (EditText)findViewById(R.id.editContactNo);
        etPassword = (EditText)findViewById(R.id.editPassword);
        btnSignUp = (Button)findViewById(R.id.buttonSignUp);
        spinDrList = (Spinner)findViewById(R.id.spinnerDoctorList);
        radioSexGroup = (RadioGroup)findViewById(R.id.radioSex);
        radioRoleGroup = (RadioGroup)findViewById(R.id.radioRole);

        spinDrList = (Spinner)findViewById(R.id.spinnerDoctorList);
        spinDrList.setOnItemSelectedListener(this);

        spinnerDrSelect = (LinearLayout)findViewById(R.id.spinnerLayout);
        spinnerDrSelect.setVisibility(LinearLayout.GONE);

        DoctorList = getDoctorTableValues();
        final ArrayAdapter my_Adapter = new ArrayAdapter(this, R.layout.spinner_row, DoctorList);

        final InsertIntoDatabase insertIntoDatabase = new InsertIntoDatabase();

        radioRoleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioRoleButton = (RadioButton) findViewById(checkedId);
                if (radioRoleButton.getText().toString().equals("Patient")) {
                    spinnerDrSelect.setVisibility(LinearLayout.VISIBLE);
                    spinDrList.setAdapter(my_Adapter);
                    strRole = "Patient";
                } else {
                    spinnerDrSelect.setVisibility(LinearLayout.GONE);
                    strRole = "Doctor";
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                strName = etName.getText().toString();
                strEmail = etEmail.getText().toString();
                strContact = etContact.getText().toString();
                strPassword = etPassword.getText().toString();

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton)findViewById(selectedId);
                strGender = radioSexButton.getText().toString();

                if (strRole == "Patient")
                {
                    sql = "INSERT INTO Patients (Name, Email, Password, DOB, Contact, Gender, Doctor) values ('"+strName+"','"+strEmail+"','"+strPassword+"','"+strDOB+"','"+strContact+"','"+strGender+"','"+strSelectedDr+"')";

                    String sql_update = "UPDATE Doctors SET Patients=concat(Patients,'"+strName+",') WHERE Name='"+strSelectedDr+"'";
                    insertIntoDatabase.insert(sql_update);
                }
                else
                {
                    sql = "INSERT INTO Doctors (Name, Email, Password, DOB, Contact, Gender) values ('"+strName+"','"+strEmail+"','"+strPassword+"','"+strDOB+"','"+strContact+"','"+strGender+"')";
                }
                insertIntoDatabase.insert(sql);

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        strSelectedDr = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public ArrayList<String> getDoctorTableValues() {

        ArrayList<String> my_array = new ArrayList<String>();
        String sql = "SELECT * FROM Doctors";
        GetFromDatabase getFromDatabase = new GetFromDatabase();
        JsonString = getFromDatabase.GetData(sql, FileName.ServerPHP.Doctor);
        showListDoctor(JsonString, my_array);
        return my_array;
    }

    protected void showListDoctor(String myJSON, ArrayList<String> my_array){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            doctors = jsonObj.getJSONArray("result");

            for(int i=0;i<doctors.length();i++){
                JSONObject c = doctors.getJSONObject(i);
                String name = c.getString("name");
                my_array.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    year = arg1;
                    month = arg2+1;
                    day = arg3;
                    showDate(arg1, arg2+1, arg3);
                    strDOB = month+"/"+day+"/"+year;
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


}