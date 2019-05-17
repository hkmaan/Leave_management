package com.harpreet.leave_management;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Hr_profile_page extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 300;
    EditText name_et;
    EditText gender_et;
    EditText DOB_et;
    EditText phone_et;
    EditText address_et;
    EditText email_et;
    TextView edit_details;
    Button save_details;
    Button change_password;
    private static ProgressDialog pd;
    FirebaseDatabase database;
    String email;

    ImagePicker imagePicker;
    CircleImageView img;
    String imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_profile_page);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
        Hr_profile_page.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        name_et = findViewById(R.id.hr_name);
        gender_et = findViewById(R.id.editText1);
        DOB_et = findViewById(R.id.editText2);
        phone_et = findViewById(R.id.editText3);
        address_et = findViewById(R.id.editText4);
        email_et = findViewById(R.id.editText5);
        edit_details = findViewById(R.id.edit_details);
        save_details = findViewById(R.id.save_changes);
        change_password = findViewById(R.id.button);
        pd.show();
        get_data_firebase();

        ImagePicker.setMinQuality(600, 600);
        img = findViewById(R.id.circle_image);


    }

    private void get_data_firebase() {
        database = FirebaseDatabase.getInstance();

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DatabaseReference reference = database.getReference("hr_profile").child(email.replace(".",""));
        //DatabaseReference reference = database.getReference("hr_profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pd.hide();
                if(dataSnapshot.child("hr_name").exists()){
                    String name = dataSnapshot.child("hr_name").getValue().toString();
                    name_et.setText(name);
                }
                if(dataSnapshot.child("DOB").exists()){
                    String DOB = dataSnapshot.child("DOB").getValue().toString();
                    DOB_et.setText(DOB);
                }


                if(dataSnapshot.child("Address").exists())
                {
                    String Address = dataSnapshot.child("Address").getValue().toString();
                    address_et.setText(Address);
                }
                if (dataSnapshot.child("email").exists()){
                    String email = dataSnapshot.child("email").getValue().toString();
                    email_et.setText(email);
                }
               if (dataSnapshot.child("phone_no").exists()){
                   String phone_no = dataSnapshot.child("phone_no").getValue().toString();
                   phone_et.setText(phone_no);
               }
               if ( dataSnapshot.child("gender").exists()){
                   String gender = dataSnapshot.child("gender").getValue().toString();

                   gender_et.setText(gender);
               }

                if(dataSnapshot.child("image").exists()) {
                    Glide.with(Hr_profile_page.this).load(dataSnapshot.child("image").getValue().toString()).into(img);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }





    public void hr_back(View view) {
        finish();
    }

    public void edit_details(View view) {

        name_et.setEnabled(true);
        gender_et.setEnabled(true);
        DOB_et.setEnabled(true);
        phone_et.setEnabled(true);
        address_et.setEnabled(true);
        edit_details.setEnabled(false);
        save_details.setVisibility(View.VISIBLE);
        change_password.setVisibility(View.VISIBLE);


        InputMethodManager inputMethodManager = (InputMethodManager) Hr_profile_page.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(name_et.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        name_et.requestFocus();
    }

    public void save_details(View view) {
        String hr_name = name_et.getText().toString();
        String Address = address_et.getText().toString();
        String phone = phone_et.getText().toString();
        String DOB = DOB_et.getText().toString();
        email = email_et.getText().toString();
        String gender = gender_et.getText().toString();
        if (hr_name.equals(""))
        {
            Toast.makeText(Hr_profile_page.this , "enter your name" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (Address.equals(""))
        {
            Toast.makeText(Hr_profile_page.this , "enter your Address" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.equals(""))
        {
            Toast.makeText(Hr_profile_page.this , "enter your Phone Number " , Toast.LENGTH_SHORT).show();
            return;
        }
        if (DOB.equals(""))
        {
            Toast.makeText(Hr_profile_page.this , "enter your DOB" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (gender.equals(""))
        {
            Toast.makeText(Hr_profile_page.this , "enter your gender" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUrl.equals("")){
            Toast.makeText(Hr_profile_page.this , "Upload your image" , Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference myRef = database.getReference("hr_profile").child(email.replace(".",""));
        Hr_details details = new Hr_details(hr_name, Address, email, gender, DOB, phone);

        details.image = imageUrl;

        name_et.setEnabled(false);
        gender_et.setEnabled(false);
        DOB_et.setEnabled(false);
        phone_et.setEnabled(false);
        address_et.setEnabled(false);

        save_details.setVisibility(View.INVISIBLE);
        change_password.setVisibility(View.INVISIBLE);
        OnCompleteListener<Void> Listner = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Hr_profile_page.this, "UPDATED SUCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Hr_profile_page.this, "NOT UPDATED SUCESSFULLY", Toast.LENGTH_SHORT).show();
                }

            }
        };
        myRef.setValue(details).addOnCompleteListener(Listner);
    }

    public void change_password(View view) {
        Intent i = new Intent(Hr_profile_page.this, Change_passowrd.class);
        startActivity(i);


    }

    public void onPickImage(View view) {
        imagePicker.pickImage(Hr_profile_page.this, "Select your image:");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            img.setImageBitmap(bitmap);
            uploadFile(bitmap);
        }


    }

    private void uploadFile(Bitmap bitmap) {

        pd.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("images/" + String.valueOf( System.currentTimeMillis() ) + ".jpg");
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