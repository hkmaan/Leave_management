package com.harpreet.leave_management;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sick_leave extends AppCompatActivity {

    TextView name1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_leave);
        name1=findViewById(R.id.stxt5);

       // String to_date = getIntent().getStringExtra("to_date");

        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String name = sp.getString("emp_name" , "");
        name1.setText(name.toUpperCase());
      /*  database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(name);*/

    }

    public void sback(View view) {
        finish();
    }



}
