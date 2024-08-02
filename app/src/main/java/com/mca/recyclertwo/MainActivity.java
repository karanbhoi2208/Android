package com.mca.recyclertwo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText employeeNameEdt, employeePhoneEdt, employeeAddressEdt;
    private Button sendDatabtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EmployeeInfo employeeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        employeeNameEdt = findViewById(R.id.idEdtEmployeeName);
        employeePhoneEdt = findViewById(R.id.idEdtEmployeePhoneNumber);
        employeeAddressEdt = findViewById(R.id.idEdtEmployeeAddress);
        sendDatabtn = findViewById(R.id.idBtnSendData);

        // Getting instance of Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Getting reference to "EmployeeInfo" node
        databaseReference = firebaseDatabase.getReference("EmployeeInfo");

        // Initializing employeeInfo object
        employeeInfo = new EmployeeInfo();

        // Setting OnClickListener to send data button
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting values from EditText fields
                String name = employeeNameEdt.getText().toString();
                String phone = employeePhoneEdt.getText().toString();
                String address = employeeAddressEdt.getText().toString();

                // if any field is empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                    Toast.makeText(MainActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    // Adding data to Firebase
                    addDatatoFirebase(name, phone, address);
                }
            }
        });
    }

    private void addDatatoFirebase(String name, String phone, String address) {
        // Setting values to employeeInfo object
        employeeInfo.setEmployeeName(name);
        employeeInfo.setEmployeeContactNumber(phone);
        employeeInfo.setEmployeeAddress(address);

        // Pushing data to Firebase under a unique ID
        databaseReference.push().setValue(employeeInfo)
                .addOnSuccessListener(aVoid -> {
                    // Showing toast on successful data addition
                    Toast.makeText(MainActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Showing toast on failure
                    Toast.makeText(MainActivity.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();
                });
    }
}
