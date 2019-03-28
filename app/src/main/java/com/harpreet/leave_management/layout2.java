package com.harpreet.leave_management;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class layout2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout2);
    }

    public void manager_image(View view) {
        Intent i = new Intent(layout2.this , hr_login3.class);
        startActivity(i);
    }

    public void employee_image(View view) {
        Intent i = new Intent(layout2.this , Employee_login.class);
        startActivity(i);
    }
}
