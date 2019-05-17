package com.harpreet.leave_management;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Apply_for_Leave extends AppCompatActivity {

    private Calendar mcalendar;
    private Calendar mcalendar1;
    private EditText mdob_et;
    private EditText mdob_et1;
    private int day,month,year;
    EditText e_id;
    EditText from_et;
    EditText to_et;
    EditText Reason_et;
    String empid;
    String emp_name,email;
    EditText name;
    String s_designation;
    EditText designation;
    //spinner
    private Spinner Leave_spinner;

    int APPLIED_LEAVE_NUMBER_SICK = 0;
    int APPLIED_LEAVE_NUMBER_CASUAL = 0;
    int APPLIED_LEAVE_NUMBER_PAID = 0;
    int APPLIED_LEAVE_NUMBER_OTHER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for__leave);
            name=findViewById(R.id.name);
            designation=findViewById(R.id.e_designation);
        e_id=findViewById(R.id.empid);
        Leave_spinner=findViewById(R.id.spinner);
        from_et=findViewById(R.id.from1);
        to_et=findViewById(R.id.to1);
        Reason_et=findViewById(R.id.reason_txt);

        SharedPreferences sp=getSharedPreferences("employee_info",MODE_PRIVATE);
         empid=sp.getString("emp_id","");
        e_id.setText(empid);
        e_id.setEnabled(false);
        emp_name=sp.getString("emp_name","");
        name.setText(emp_name);
        name.setEnabled(false);
        s_designation=sp.getString("designation","");
        designation.setText(s_designation);
        designation.setEnabled(false);
         email = sp.getString("email", "");
        //Spinner
        Leave_spinner=findViewById(R.id.spinner);
        Leave_spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner_item,getResources().getStringArray(R.array.Types_of_Leaves));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Leave_spinner.setAdapter(adapter);


            //calendar date picker
        mcalendar= Calendar.getInstance();
        mcalendar1= Calendar.getInstance();
        mdob_et=findViewById(R.id.from1);
        mdob_et1=findViewById(R.id.to1);
        mdob_et.setOnClickListener(mClickListener);
        mdob_et1.setOnClickListener(mClickListener1);
        day=  mcalendar.get(Calendar.DAY_OF_MONTH);
        day=  mcalendar1.get(Calendar.DAY_OF_MONTH);
        year=  mcalendar.get(Calendar.YEAR);
        year=  mcalendar1.get(Calendar.YEAR);
        month=  mcalendar.get(Calendar.MONTH);
        month=  mcalendar1.get(Calendar.MONTH);

        get_applied_leave_number();
    }
    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog();
        }
    };

    View.OnClickListener mClickListener1=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog1();
        }
    };

    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                mdob_et.setText(dayOfMonth + "/" + (monthOfYear +1)+ "/" + year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(Apply_for_Leave.this, listener, year, month, day);
        mcalendar.set(2018,0,1);
        dpDialog.getDatePicker().setMinDate(mcalendar.getTimeInMillis());
        mcalendar.set(2050,11,31);
        dpDialog.getDatePicker().setMaxDate(mcalendar.getTimeInMillis());
        dpDialog.show();
    }
    public void DateDialog1(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                mdob_et1.setText(dayOfMonth + "/" + (monthOfYear +1)+ "/" + year);
            }};
        DatePickerDialog dpDialog1=new DatePickerDialog(Apply_for_Leave.this, listener, year, month, day);
        mcalendar1.set(2018,0,1);
        dpDialog1.getDatePicker().setMinDate(mcalendar1.getTimeInMillis());
        mcalendar1.set(2050,11,31);
        dpDialog1.getDatePicker().setMaxDate(mcalendar1.getTimeInMillis());
        dpDialog1.show();
    }

    public void apply(View view) {

        String leave_type = Leave_spinner.getSelectedItem().toString();

        if( leave_type.equalsIgnoreCase("Sick Leave") && APPLIED_LEAVE_NUMBER_SICK >= 12 )
        {
            Toast.makeText(Apply_for_Leave.this ,  "quota full" , Toast.LENGTH_SHORT).show();
            return;
        }

        if( leave_type.equalsIgnoreCase("Paid Leave") && APPLIED_LEAVE_NUMBER_PAID >= 12 )
        {
            Toast.makeText(Apply_for_Leave.this ,  "quota full" , Toast.LENGTH_SHORT).show();
            return;
        }
        if( leave_type.equalsIgnoreCase("Casual Leave") && APPLIED_LEAVE_NUMBER_CASUAL >= 12 )
        {
            Toast.makeText(Apply_for_Leave.this ,  "quota full" , Toast.LENGTH_SHORT).show();
            return;
        }
        if( leave_type.equalsIgnoreCase("Other Leave") && APPLIED_LEAVE_NUMBER_PAID >= 12 )
        {
            Toast.makeText(Apply_for_Leave.this ,  "quota full" , Toast.LENGTH_SHORT).show();
            return;
        }


        String from_s=from_et.getText().toString();
        String to_s=to_et.getText().toString();
        String Reason_s=Reason_et.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Leave_DataModel leaveDataModel=new Leave_DataModel(emp_name,s_designation,empid,leave_type,from_s,to_s,Reason_s,"pending",email);
        DatabaseReference reference = database.getReference("leaves").child( empid );


        OnCompleteListener<Void> Listner = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i= new  Intent(Apply_for_Leave.this,Request_sent.class);
                    Toast.makeText(Apply_for_Leave.this, "sucessfully applied", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }

            }
        };


        reference.push().setValue(leaveDataModel).addOnCompleteListener(Listner);


    }

    public void lback(View view) {
        finish();
    }

    public void see_leave_format(View view) {

        String leave_type = Leave_spinner.getSelectedItem().toString();

        if (leave_type.equalsIgnoreCase("Sick Leave")) {
            Intent i = new Intent(Apply_for_Leave.this, Sick_leave.class);

            i.putExtra("to_date", to_et.getText().toString());
            i.putExtra("from_date", from_et.getText().toString());

            startActivity(i);
        }
        if (leave_type.equalsIgnoreCase("Paid Leave")) {
            Intent intent = new Intent(Apply_for_Leave.this, Paid_leave.class);
            intent.putExtra("to_date", to_et.getText().toString());
            intent.putExtra("from_date", from_et.getText().toString());
            startActivity(intent);
        }
        if (leave_type.equalsIgnoreCase("Casual Leave")) {
            Intent intent = new Intent(Apply_for_Leave.this,Casual_leave.class);
            intent.putExtra("to_date", to_et.getText().toString());
            intent.putExtra("from_date", from_et.getText().toString());
            startActivity(intent);
        }



    }

    private void get_applied_leave_number()
    {
        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String empid = sp.getString("emp_id", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("leaves").child(empid.replace(".", ""));



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for( DataSnapshot snapshot : dataSnapshot.getChildren() )
                {
                    Leave_DataModel dataLeaveModel = snapshot.getValue(Leave_DataModel.class);

                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Sick Leave"))
                    {
                        APPLIED_LEAVE_NUMBER_SICK ++;
                    }

                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Paid Leave"))
                    {
                        APPLIED_LEAVE_NUMBER_PAID ++;
                    }
                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Casual Leave"))
                    {
                        APPLIED_LEAVE_NUMBER_CASUAL ++;
                    }
                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Other Leave"))
                    {
                        APPLIED_LEAVE_NUMBER_OTHER ++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
