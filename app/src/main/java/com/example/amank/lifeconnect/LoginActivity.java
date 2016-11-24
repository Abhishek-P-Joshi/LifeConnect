package com.example.amank.lifeconnect;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    RadioGroup radioButtonGroup;
    RadioButton r;
    String userselectText;
    Button btnsignup;
    //private static final String TAG_PASSWORD = "Password";
    //private static final String TAG_NAME = "Name";
    private static final String TAG_RESULTS="result";

    JSONArray values = null;
    private String authenticate;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    //@InjectView(R.id.link_signup)
    //TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ButterKnife.inject(this);
        radioButtonGroup = (RadioGroup) findViewById(R.id.user_selection);
        btnsignup = (Button) findViewById(R.id.btn_signup);
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION },1);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void login(String username, String password, String userSelection) {
        //boolean flag = false;
        //Log.d(TAG, "Login");
        if(userSelection.equals("Doctor")) {
            String sql = "SELECT Email,Password FROM Doctors WHERE Email ='" + username + "' AND Password ='" + password + "'";
            GetFromDatabase getFromDatabase = new GetFromDatabase();
            authenticate = getFromDatabase.GetData(sql, FileName.ServerPHP.Doctor);
            try {
                JSONObject jsonObj = new JSONObject(authenticate);
                values = jsonObj.getJSONArray(TAG_RESULTS);
                if (values.length() > 0) {
                    /*Intent startSenseService = new Intent(LoginActivity.this, SensorHandler.class);
                    Bundle b = new Bundle();
                    b.putString("name", parseName(username));
                    startSenseService.putExtras(b);
                    startService(startSenseService);*/
                    //flag = true;
                    //Intent intent = new Intent(this, DemoInsert.class);
                    //startActivity(intent);
                    Intent intent = new Intent(this, SelectPatient.class);
                    Bundle bu = new Bundle();
                    bu.putString("email", username);
                    intent.putExtras(bu);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Please Enter Proper Credentials!", Toast.LENGTH_SHORT).show();
                    onLoginFailed();
                }
//            JSONObject c = values.getJSONObject(0);
//            String name = c.getString(TAG_NAME);
//            String passwd = c.getString(TAG_PASSWORD);
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else if(userSelection.equals("Patient")){
            String sql = "SELECT Email,Password FROM Patients WHERE Email ='" + username + "' AND Password ='" + password+"'";
            GetFromDatabase getFromDatabase = new GetFromDatabase();
            authenticate = getFromDatabase.GetData(sql, FileName.ServerPHP.Patient);
            try {
                JSONObject jsonObj = new JSONObject(authenticate);
                values = jsonObj.getJSONArray(TAG_RESULTS);
                if(values.length()>0)
                {
                    Intent startSenseService = new Intent(LoginActivity.this, SensorHandler.class);
                    Bundle b = new Bundle();
                    b.putString("name", username);
                    startSenseService.putExtras(b);
                    startService(startSenseService);
                    //flag = true;
                    //Intent intent = new Intent(this, DemoInsert.class);
                    //startActivity(intent);
                    Intent intent = new Intent(this, Patient_Dashboard.class);
                    Bundle bu = new Bundle();
                    bu.putString("name", username);
                    intent.putExtras(bu);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(),"Please Enter Proper Credentials!",Toast.LENGTH_SHORT).show();
                    onLoginFailed();
                }
//            JSONObject c = values.getJSONObject(0);
//            String name = c.getString(TAG_NAME);
//            String passwd = c.getString(TAG_PASSWORD);
            }catch(Exception e){
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        _loginButton.setEnabled(true);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

            //String email = _emailText.getText().toString();
            //String password = _passwordText.getText().toString();
        }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void validate() {
        boolean valid = true;
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonID);
        int idx = radioButtonGroup.indexOfChild(radioButton);
        r = (RadioButton)radioButtonGroup.getChildAt(idx);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            _passwordText.setError("Password between 4 and 12 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (idx < 0) {
            Toast.makeText(LoginActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            userselectText = r.getText().toString();
        }
        if(valid == true){
            login(email,password,userselectText);
        }else{
            onLoginFailed();
        }

        //return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
