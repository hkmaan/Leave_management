package com.harpreet.leave_management;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Employeelogin_p extends AppCompatActivity {

    EditText email_et , password_et;
    TextView show_pswd;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeelogin_p);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        email_et = (EditText) findViewById(R.id.eemail);
        password_et = (EditText) findViewById(R.id.password);
        show_pswd = (TextView) findViewById(R.id.show_pswrd);


        show_pswd.setVisibility(View.GONE);
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password_et.getText().length()>0){
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
                    password_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password_et.setSelection(password_et.length());
                }
                else{
                    show_pswd.setText("SHOW");
                    password_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_et.setSelection(password_et.length());
                }
            }
        });


        SharedPreferences sp=getSharedPreferences("employee_info",MODE_PRIVATE);

        if(sp.getString("email" , "").equalsIgnoreCase(""))
        {

        }

        else {

            Intent i = new Intent(Employeelogin_p.this , Home_employee.class);

            startActivity(i);

            finish();


        }



    }



    public void log_in(View view) {
       final String e=email_et.getText().toString();
        final String p=password_et.getText().toString();
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$";
        if (e.isEmpty() && p.isEmpty()){
            Toast.makeText(Employeelogin_p.this,"fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }
        if( e.trim().equals("") )

        {
            Toast.makeText(Employeelogin_p.this , "Enter your E-mail" , Toast.LENGTH_SHORT).show();

            return;
        }
        if (p.equals(""))
        {
            Toast.makeText(Employeelogin_p.this , "enter your password" , Toast.LENGTH_SHORT).show();
            return;
        }
        if(!p.matches(pattern) || password_et.length() < 8)
        {
            Toast.makeText(Employeelogin_p.this, "password must contain atleast one alphabet , digit , special character and length must be 8 character", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches())
        {
            Toast.makeText(Employeelogin_p.this , "enter your email" , Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("employees").child(e.replace(".",""));

        progressBar.setVisibility(View.VISIBLE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Add_emp_data dataModel = dataSnapshot.getValue(Add_emp_data.class);

                    if (dataModel.password.equals(p)) {
                        SharedPreferences.Editor sp=getSharedPreferences("employee_info",MODE_PRIVATE).edit();
                        sp.putString("email",e);
                        sp.putString("emp_id",dataModel.emp_id);
                        sp.putString("emp_name",dataModel.emp_name);
                        sp.putString("designation",dataModel.designation);
                        sp.commit();
                        Intent intent = new Intent(Employeelogin_p.this, Home_employee.class);
                        Toast.makeText(Employeelogin_p.this, "logged in", Toast.LENGTH_LONG).show();
                        startActivity(intent);

                    }else {
                        Toast.makeText(Employeelogin_p.this, "please enter correct password", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                }else {
                    Toast.makeText(Employeelogin_p.this, "Email donot exist", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });






    }
    public void forgot_password(View view) {
        Intent intent= new Intent(Employeelogin_p.this,Resetpassword.class);
        startActivity(intent);
    }
}
