package com.harpreet.leave_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class Leave_format extends AppCompatActivity {
    private Calendar mcalendar;
    private Calendar mcalendar1;
    private EditText mdob_et;
    private EditText mdob_et1;
    private int day, month, year;
    EditText e_id;
    EditText from_et;
    EditText to_et;
    EditText Reason_et;
    String empid;
    String emp_name;
    EditText name;
    String s_designation,leave_type;
    TextView designation, Leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_format);
        Leave_format.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        name = findViewById(R.id.name5);
        designation = findViewById(R.id.e_designation5);
        e_id = findViewById(R.id.empid5);
        Leave=findViewById(R.id.type_text);
        from_et = findViewById(R.id.from2);
        to_et = findViewById(R.id.to2);
        Reason_et = findViewById(R.id.reason_txt1);

        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        empid = sp.getString("emp_id", "");
        e_id.setText(empid);
        e_id.setEnabled(false);
        Reason_et.setEnabled(false);
        emp_name = sp.getString("emp_name", "");
        name.setText(emp_name);
        name.setEnabled(false);
        s_designation = sp.getString("designation", "");
        designation.setText(s_designation);
        designation.setEnabled(false);

        //Spinner


        //calendar date picker
        mcalendar = Calendar.getInstance();
        mcalendar1 = Calendar.getInstance();
        mdob_et = findViewById(R.id.from2);
        mdob_et1 = findViewById(R.id.to2);

        from_et.setText(getIntent().getStringExtra("from"));

leave_type=getIntent().getStringExtra("type");
        to_et.setText(getIntent().getStringExtra("to"));
Leave.setText(getIntent().getStringExtra("type"));
        Reason_et.setText(getIntent().getStringExtra("Reason"));


        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        day = mcalendar1.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        year = mcalendar1.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        month = mcalendar1.get(Calendar.MONTH);
    }




    public void see_leave_format(View view) {



        if (leave_type.equalsIgnoreCase("Sick Leave")) {
            Intent i = new Intent(Leave_format.this, Sick_leave.class);

            i.putExtra("to_date", to_et.getText().toString());
            i.putExtra("from_date", from_et.getText().toString());

            startActivity(i);
        }
        if (leave_type.equalsIgnoreCase("Paid Leave")) {
            Intent intent = new Intent(Leave_format.this, Paid_leave.class);
            intent.putExtra("to_date", to_et.getText().toString());
            intent.putExtra("from_date", from_et.getText().toString());
            startActivity(intent);
        }
        if (leave_type.equalsIgnoreCase("Casual Leave")) {
            Intent intent = new Intent(Leave_format.this, Paid_leave.class);
            intent.putExtra("to_date", to_et.getText().toString());
            intent.putExtra("from_date", from_et.getText().toString());
            startActivity(intent);
        }




    }

    public void back1(View view) {
        finish();
    }
}