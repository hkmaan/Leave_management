package com.harpreet.leave_management;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Casual_leave extends AppCompatActivity {
        TextView from_d;
        TextView to_d;
        TextView name3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casual_leave);

        from_d=findViewById(R.id.from1);
        to_d=findViewById(R.id.to2);
        name3=findViewById(R.id.text8);

        String to_date = getIntent().getStringExtra("to_date");
        from_d.setText(to_date);

        String from_date=getIntent().getStringExtra("from_date");
        to_d.setText(from_date);

        SharedPreferences sp = getSharedPreferences("employee_info",MODE_PRIVATE);

        String name = sp.getString("emp_name" , "");
        name3.setText(name.toUpperCase());

    }

    public void cback(View view) {

        finish();
    }
}
