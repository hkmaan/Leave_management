package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Resetpassword extends AppCompatActivity {
    TextView email;
    String email_s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        email=(EditText)findViewById(R.id.reg_email);
    }

    public void submit(View view) {

        email_s=  email.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email_s)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( Resetpassword.this ,"Email Sent!!!Check Email!!" , Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else {
                            Toast.makeText(Resetpassword.this ,"error" ,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void rback(View view) {
        finish();
    }
}
