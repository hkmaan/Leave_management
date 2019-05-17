package com.harpreet.leave_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class layout2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout2);

    }

    public void manager_image(View view) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null) {
            Intent i = new Intent(layout2.this , Home_hr.class);
            startActivity(i);
        }else{
            Intent i = new Intent(layout2.this , hr_login3.class);
            startActivity(i);
        }

    }

    public void employee_image(View view) {
        Intent i = new Intent(layout2.this , Employeelogin_p.class);
        startActivity(i);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
