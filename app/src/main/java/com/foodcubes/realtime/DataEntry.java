package com.foodcubes.realtime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataEntry extends AppCompatActivity {

    String UserId;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    Spinner mealSpinner, daySpinner;
    Button addButton;
    Data mData;
    TextInputLayout menuEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        mealSpinner = findViewById(R.id.spinnerMeal);
        daySpinner = findViewById(R.id.spinnerDay);
        addButton = findViewById(R.id.showButton);
        UserId = getIntent().getStringExtra("Uid");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(UserId);
        menuEditText = findViewById(R.id.dataEditText);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDatabase();
            }
        });
    }

    private void addToDatabase() {
        mealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mData.setMeal(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mData.setDay(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        mData.setMenu(menuEditText.getEditText().getText().toString());
        mDatabaseReference.setValue(mData);

    }


}
