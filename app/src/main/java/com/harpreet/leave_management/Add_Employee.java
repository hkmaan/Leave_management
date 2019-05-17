package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Employee extends AppCompatActivity {
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__employee);

        database = FirebaseDatabase.getInstance();
    }


    public void add_emp(View view) {

        EditText empname_edit = findViewById(R.id.emp_name);
        EditText designation_edit = findViewById(R.id.designation);
        EditText empid_edit = findViewById(R.id.emp_id);
        EditText empemail_edit = findViewById(R.id.emp_email);
        EditText emppass_edit = findViewById(R.id.emp_pass);


        String emp_name = empname_edit.getText().toString();
        String designation = designation_edit.getText().toString();
        String emp_id = empid_edit.getText().toString();
        String email = empemail_edit.getText().toString();
        String password = emppass_edit.getText().toString();


        DatabaseReference myRef = database.getReference("employees");
        DatabaseReference ref2 = myRef.child(email.replace(".",""));


        Add_emp_data dataModel = new Add_emp_data(emp_name, designation,emp_id, email, password);


        ref2.setValue(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Add_Employee.this,"DONE",Toast.LENGTH_SHORT).show();


                    finish();
                }

            }
        });


    }

    public void addempback(View view) {
        finish();
    }

}

