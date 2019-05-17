package com.harpreet.leave_management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class About_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void aback(View view) {
        finish();
    }
}
