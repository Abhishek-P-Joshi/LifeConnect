package com.example.amank.lifeconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class DemoInsert extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_insert);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextPlace = (EditText) findViewById(R.id.editTextPlace);
    }

    public void insert(View view) {
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();
        String place = editTextPlace.getText().toString();

        String sql = "INSERT INTO Demo (Name, Age, Place) values ('"+name+"','"+age+"','"+place+"')";

        InsertIntoDatabase insertIntoDatabase = new InsertIntoDatabase();
        insertIntoDatabase.insert(sql);
    }

    public void GetDemo(View view) {
        Intent intent = new Intent(this,DemoGet.class);
        startActivity(intent);
    }
}
