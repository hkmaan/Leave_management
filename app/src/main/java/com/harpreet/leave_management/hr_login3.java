package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class hr_login3 extends AppCompatActivity {
  FirebaseAuth auth;
  ProgressBar progressBar;
    TextView show_pswd;
    EditText email;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_login3);
        auth =FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        show_pswd = (TextView) findViewById(R.id.show_pswrd);

        show_pswd.setVisibility(View.GONE);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().length()>0){
                    show_pswd.setVisibility(View.VISIBLE);
                }
                else {
                    show_pswd.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        show_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_pswd.getText()=="SHOW"){
                    show_pswd.setText("HIDE");
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.length());
                }
                else{
                    show_pswd.setText("SHOW");
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());
                }
            }
        });
    }

    public void log_in(View view) {

        String e = email.getText().toString();
        String p = password.getText().toString();
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$";

        if (e.isEmpty() && p.isEmpty()){
            Toast.makeText(hr_login3.this,"fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }
        if( e.trim().equals("") )
        {
            Toast.makeText(hr_login3.this , "enter your email" , Toast.LENGTH_SHORT).show();

            return;
        }
        if (p.equals(""))
        {
            Toast.makeText(hr_login3.this , "enter your password" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(!p.matches(pattern) || password.length() < 8)
        {
            Toast.makeText(hr_login3.this, "password must contain atleast one alphabet , digit , special character and length must be 8 character", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches())
        {
            Toast.makeText(hr_login3.this , " please enter your correct email" , Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Intent i = new Intent(hr_login3.this,Home_hr.class);
                    Toast.makeText(hr_login3.this,"LOGGED IN",Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
                else {
                    Toast.makeText(hr_login3.this,"please enter correct password",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        auth.signInWithEmailAndPassword(e , p).addOnCompleteListener(hr_login3.this,listener);

    }

    public void forgot_password(View view) {

        Intent i = new Intent(hr_login3.this,Resetpassword.class);
        startActivity(i);
    }
}

