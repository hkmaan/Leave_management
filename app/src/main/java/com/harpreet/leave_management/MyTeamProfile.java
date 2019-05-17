package com.harpreet.leave_management;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTeamProfile extends AppCompatActivity {

    TextView name_et;


    TextView designation_et;


    EditText empId_et;
    EditText gender_et;
    EditText DOB_et;
    EditText Phoneno_et;
    EditText Address_et;
    EditText empemail_edit;
    EditText Bloodgrp_et;


    ImageView eimage;
    ImagePicker imagePicker;
    private static ProgressDialog pd;
    FirebaseDatabase database;
    CircleImageView img;
    String p;
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team_profile);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
        database = FirebaseDatabase.getInstance();

        MyTeamProfile.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        name_et = findViewById(R.id.emp_name);
        designation_et = findViewById(R.id.designation);
        empId_et = findViewById(R.id.emp_id);
        gender_et = findViewById(R.id.gender);
        DOB_et = findViewById(R.id.Dob);
        Phoneno_et = findViewById(R.id.Phoneno);
        Address_et = findViewById(R.id.address);
        empemail_edit = findViewById(R.id.empemail);
        Bloodgrp_et = findViewById(R.id.blood_grp);

        eimage = findViewById(R.id.edit_img);

        pd.show();
        get_data_firebase();



        img = findViewById(R.id.circle_image);

    }

    private void get_data_firebase() {

        String email = getIntent().getStringExtra("email");


        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pd.hide();

                String designation = dataSnapshot.child("designation").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String id = dataSnapshot.child("emp_id").getValue().toString();
                String name = dataSnapshot.child("emp_name").getValue().toString();
                p = dataSnapshot.child("password").getValue().toString();
                if (dataSnapshot.child("gender").getValue() != null) {
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    gender_et.setText(gender);
                }
                if (dataSnapshot.child("DOB").getValue() != null) {
                    String DOB = dataSnapshot.child("DOB").getValue().toString();
                    DOB_et.setText(DOB);
                }
                if (dataSnapshot.child("Phoneno").getValue() != null) {
                    String phoneno = dataSnapshot.child("Phoneno").getValue().toString();
                    Phoneno_et.setText(phoneno);
                }
                if (dataSnapshot.child("Address").getValue() != null) {
                    String Address = dataSnapshot.child("Address").getValue().toString();
                    Address_et.setText(Address);
                }
                if (dataSnapshot.child("bloodgrp").getValue() != null) {
                    String bloodgrp = dataSnapshot.child("bloodgrp").getValue().toString();
                    Bloodgrp_et.setText(bloodgrp);

                }
                if (dataSnapshot.child("image").exists()) {

                    Glide.with(MyTeamProfile.this).load(dataSnapshot.child("image").getValue().toString()).into(img);
                }
                designation_et.setText(designation);
                empemail_edit.setText(email);
                empId_et.setText(id);
                name_et.setText(name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }








    public void emppro_back(View view) {
        finish();
    }


}
