package com.harpreet.leave_management;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Request_sent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sent);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                Intent i = new Intent(Request_sent.this, Home_employee.class);

                startActivity(i);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
