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

public class Employee_profile extends AppCompatActivity {


    TextView name_et;


    TextView designation_et;


    EditText empId_et;
    EditText gender_et;
    EditText DOB_et;
    EditText Phoneno_et;
    EditText Address_et;
    EditText empemail_edit;
    EditText Bloodgrp_et;
    TextView edit_details;
    Button save_details;
    Button change_password;
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
        setContentView(R.layout.activity_employee_profile);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
        database = FirebaseDatabase.getInstance();

        Employee_profile.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        name_et = findViewById(R.id.emp_name);
        designation_et = findViewById(R.id.designation);
        empId_et = findViewById(R.id.emp_id);
        gender_et = findViewById(R.id.gender);
        DOB_et = findViewById(R.id.Dob);
        Phoneno_et = findViewById(R.id.Phoneno);
        Address_et = findViewById(R.id.address);
        empemail_edit = findViewById(R.id.empemail);
        Bloodgrp_et = findViewById(R.id.blood_grp);
        edit_details = findViewById(R.id.edit_txt);
        save_details = findViewById(R.id.button_details);
        change_password = findViewById(R.id.button);
        eimage = findViewById(R.id.edit_img);

        pd.show();
        get_data_firebase();


        ImagePicker.setMinQuality(600, 600);
        img = findViewById(R.id.circle_image);

    }

    private void get_data_firebase() {
        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String email = sp.getString("email", "");

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

                    Glide.with(Employee_profile.this).load(dataSnapshot.child("image").getValue().toString()).into(img);
                }
                else
                {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.image));
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


    public void change_photo(View view) {
        imagePicker.pickImage(Employee_profile.this, "Select your image:");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            img.setImageBitmap(bitmap);
            uploadFile(bitmap);
   }

    }

    public void edit_edetails(View view) {
        name_et.setEnabled(true);
        designation_et.setEnabled(true);
        gender_et.setEnabled(true);
        DOB_et.setEnabled(true);
        Phoneno_et.setEnabled(true);
        Address_et.setEnabled(true);
        Bloodgrp_et.setEnabled(true);
        edit_details.setVisibility(View.INVISIBLE);
        save_details.setVisibility(View.VISIBLE);
        change_password.setVisibility(View.VISIBLE);
        eimage.setVisibility(View.INVISIBLE);

        InputMethodManager inputMethodManager = (InputMethodManager) Employee_profile.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(name_et.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        name_et.requestFocus();

    }

    public void save_details(View view) {
        String designation = designation_et.getText().toString();
        String eemail = empemail_edit.getText().toString();
        String empid = empId_et.getText().toString();
        String name = name_et.getText().toString();
        String gender = gender_et.getText().toString();
        String DOB = DOB_et.getText().toString();
        String phoneno = Phoneno_et.getText().toString();
        String Address = Address_et.getText().toString();
        String bloodgrp = Bloodgrp_et.getText().toString();
        if (designation.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your designation" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (eemail.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your email" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (empid.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your ID" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your name" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (gender.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your gender" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (DOB.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your DOB" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneno.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your Phone Number" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (Address.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your Address" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (bloodgrp.equals(""))
        {
            Toast.makeText(Employee_profile.this , "enter your Blood Group" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUrl.equals(""))
        {
            Toast.makeText(Employee_profile.this , "Upload your Photo" , Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String email = sp.getString("email", "");
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));
        Emp_data data = new Emp_data(designation, eemail, p, empid, name, gender, DOB, phoneno, Address, bloodgrp);
        data.image = imageUrl;
        name_et.setEnabled(false);

        designation_et.setEnabled(false);

        gender_et.setEnabled(false);
        DOB_et.setEnabled(false);
        Phoneno_et.setEnabled(false);
        Address_et.setEnabled(false);
        Bloodgrp_et.setEnabled(false);
        edit_details.setVisibility(View.VISIBLE);
        save_details.setVisibility(View.INVISIBLE);
        change_password.setVisibility(View.INVISIBLE);
        eimage.setVisibility(View.VISIBLE);

        OnCompleteListener<Void> Listner = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Employee_profile.this, "UPDATED SUCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Employee_profile.this, "NOT UPDATED SUCESSFULLY", Toast.LENGTH_SHORT).show();
                }

            }
        };
        reference.setValue(data).addOnCompleteListener(Listner);
    }

    public void changee_password(View view) {
        Intent i = new Intent(Employee_profile.this, echange_password.class);
        startActivity(i);
    }

    public void emppro_back(View view) {
        finish();
    }

    private void uploadFile(Bitmap bitmap) {
pd.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("images/" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                pd.hide();
                                //createNewPost(imageUrl);
                            }
                        });
                    }
                }
            }
        });
    }
}
