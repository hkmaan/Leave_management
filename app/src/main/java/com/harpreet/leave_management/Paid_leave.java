package com.harpreet.leave_management;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Paid_leave extends AppCompatActivity {
        TextView date;
        TextView name2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_leave);
        date=findViewById(R.id.txt1);
        name2=findViewById(R.id.txt7);
        String to_date = getIntent().getStringExtra("to_date");
        date.setText(to_date);
        SharedPreferences sp = getSharedPreferences("employee_info",MODE_PRIVATE);

        String name = sp.getString("emp_name" , "");
        name2.setText(name.toUpperCase());
    }

    public void oback(View view) {
        finish();
    }
}
