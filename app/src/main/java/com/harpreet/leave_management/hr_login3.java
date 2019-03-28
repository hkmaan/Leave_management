package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class hr_login3 extends AppCompatActivity {
  FirebaseAuth auth;
  ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_login3);
        auth =FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void log_in(View view) {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        String e = email.getText().toString();
        String p = password.getText().toString();
        if (e.isEmpty() && p.isEmpty()){
            Toast.makeText(hr_login3.this,"fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Intent i = new Intent(hr_login3.this,Home_hr.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(hr_login3.this,"INVALID CREDENTIALS",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        auth.signInWithEmailAndPassword(e , p).addOnCompleteListener(hr_login3.this,listener);

    }
    }

