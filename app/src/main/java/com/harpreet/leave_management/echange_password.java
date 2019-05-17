package com.harpreet.leave_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class echange_password extends AppCompatActivity {
  FirebaseDatabase database;
    String designation;
    String eemail;
    String idd;
    String old_password;
    String name;
    String gender;
    String DOB;
    String phoneno;
    String Address;
    String bloodgrp;
    EditText old_pass;
    EditText new_pass;
    EditText confirm_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echange_password);
        old_pass=findViewById(R.id.old_edittext);
       new_pass =findViewById(R.id.newpass);
       confirm_pass=findViewById(R.id.confirm_pass);


        get_data_firebase();

    }
    private void get_data_firebase() {
        SharedPreferences sp=getSharedPreferences("employee_info",MODE_PRIVATE);
        String email= sp.getString("email","");

        System.out.print(email+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".",""));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                designation = dataSnapshot.child("designation").getValue().toString();
                eemail = dataSnapshot.child("email").getValue().toString();
                idd = dataSnapshot.child("emp_id").getValue().toString();
                name = dataSnapshot.child("emp_name").getValue().toString();
                old_password= dataSnapshot.child("password").getValue().toString();
                gender = dataSnapshot.child("gender").getValue().toString();
                DOB = Objects.requireNonNull(dataSnapshot.child("DOB").getValue()).toString();
                phoneno = dataSnapshot.child("Phoneno").getValue().toString();
                Address = dataSnapshot.child("Address").getValue().toString();
                bloodgrp = dataSnapshot.child("bloodgrp").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void BACK(View view) {
        finish();
    }

    public void chngeeeepassss(View view) {



        SharedPreferences sp=getSharedPreferences("employee_info",MODE_PRIVATE);
        String email= sp.getString("email","");
        final String oldpass=old_pass.getText().toString();
        final String newpass=new_pass.getText().toString();
        final String confirmpass=confirm_pass.getText().toString();




        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$";




        if(!newpass.matches(pattern) || new_pass.length() < 8)
        {
            Toast.makeText(echange_password.this, "password must contain atleast one alphabet , digit , special character and length must be 8 character", Toast.LENGTH_SHORT).show();
            return;
        }
        if (! confirmpass.equals(newpass))
        {

            Toast.makeText( echange_password.this ,"password do not match", Toast.LENGTH_SHORT).show();
            return;

        }
        if (! newpass.equals(oldpass)&&oldpass.equals(old_password))
        {
            FirebaseDatabase dd=FirebaseDatabase.getInstance();
            dd.getReference("employees").child(email.replace(".","")).child("password").setValue(newpass);
            Toast.makeText(echange_password.this,"password changed",Toast.LENGTH_SHORT).show();

        }else {

            Toast.makeText(echange_password.this,"Old password and new password cannot be same",Toast.LENGTH_SHORT).show();
        }
    }
}
