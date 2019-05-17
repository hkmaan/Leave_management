package com.harpreet.leave_management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Leave_policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_policy);
    }

    public void about_back(View view) {
        finish();
    }
}
