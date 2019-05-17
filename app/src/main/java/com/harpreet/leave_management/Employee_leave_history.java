package com.harpreet.leave_management;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Employee_leave_history extends AppCompatActivity {
    int APPLIED_LEAVE_NUMBER_SICK = 0;
    int APPLIED_LEAVE_NUMBER_CASUAL = 0;
    int APPLIED_LEAVE_NUMBER_PAID = 0;
    int APPLIED_LEAVE_NUMBER_OTHER = 0;
    private static ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_leave_history);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
        Leave_history();
        pd.show();
    }

    public void Leave_history() {
        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String empid = sp.getString("emp_id", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("leaves").child(empid.replace(".", ""));


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Leave_DataModel dataLeaveModel = snapshot.getValue(Leave_DataModel.class);

                    if (dataLeaveModel.Leave_type.equalsIgnoreCase("Sick Leave")) {
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_SICK++;
                        }
                    }

                    if (dataLeaveModel.Leave_type.equalsIgnoreCase("Paid Leave")) {
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_PAID++;
                        }
                    }
                    if (dataLeaveModel.Leave_type.equalsIgnoreCase("Casual Leave")) {
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_CASUAL++;
                        }
                    }
                    if (dataLeaveModel.Leave_type.equalsIgnoreCase("Other Leave")) {
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_OTHER++;
                        }
                    }
                }
                pd.hide();
                setup_pie();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setup_pie() {

        PieChartView pieChartView = findViewById(R.id.chart1);


        int paid_availabe = 12 - APPLIED_LEAVE_NUMBER_PAID;
        TextView consumed_p = findViewById(R.id.day4);
        TextView left_p = findViewById(R.id.D2);
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(paid_availabe, Color.DKGRAY));
        pieData.add(new SliceValue(APPLIED_LEAVE_NUMBER_PAID, Color.CYAN));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1(String.valueOf(paid_availabe) + " Available").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0DE9A7"));
        pieChartView.setPieChartData(pieChartData);
        consumed_p.setText(String.valueOf(APPLIED_LEAVE_NUMBER_PAID));
        left_p.setText(String.valueOf(paid_availabe));


        int casual_available = 12 - APPLIED_LEAVE_NUMBER_CASUAL;
        PieChartView pieChartView2 = findViewById(R.id.chart2);
        TextView consumed_c = findViewById(R.id.D5);
        TextView left_c = findViewById(R.id.D8);
        List pieData2 = new ArrayList<>();
        pieData2.add(new SliceValue(casual_available, Color.DKGRAY));
        pieData2.add(new SliceValue(APPLIED_LEAVE_NUMBER_CASUAL, Color.GREEN));
        PieChartData pieChartData2 = new PieChartData(pieData2);
        pieChartData2.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData2.setHasCenterCircle(true).setCenterText1(String.valueOf(casual_available) + " Available").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0DE9A7"));
        pieChartView2.setPieChartData(pieChartData2);
        consumed_c.setText(String.valueOf(APPLIED_LEAVE_NUMBER_CASUAL));
        left_c.setText(String.valueOf(casual_available));


        int sick_available = 12 - APPLIED_LEAVE_NUMBER_SICK;
        PieChartView pieChartView3 = findViewById(R.id.chart3);
        TextView consumed_s = findViewById(R.id.D11);
        TextView left_s = findViewById(R.id.D14);
        List pieData3 = new ArrayList<>();
        pieData3.add(new SliceValue(sick_available, Color.DKGRAY));
        pieData3.add(new SliceValue(APPLIED_LEAVE_NUMBER_SICK, Color.BLUE));
        PieChartData pieChartData3 = new PieChartData(pieData3);
        pieChartData3.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData3.setHasCenterCircle(true).setCenterText1(String.valueOf(sick_available) + " Available").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0DE9A7"));
        pieChartView3.setPieChartData(pieChartData3);
        consumed_s.setText(String.valueOf(APPLIED_LEAVE_NUMBER_SICK));
        left_s.setText(String.valueOf(sick_available));


        int other_available = 12 - APPLIED_LEAVE_NUMBER_OTHER;
        PieChartView pieChartView4 = findViewById(R.id.chart4);
        TextView consumed_o = findViewById(R.id.D17);
        TextView left_o = findViewById(R.id.D20);
        List pieData4 = new ArrayList<>();
        pieData4.add(new SliceValue(other_available, Color.DKGRAY));
        pieData4.add(new SliceValue(APPLIED_LEAVE_NUMBER_OTHER, Color.MAGENTA));
        PieChartData pieChartData4 = new PieChartData(pieData4);
        pieChartData4.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData4.setHasCenterCircle(true).setCenterText1(String.valueOf(other_available) + " Available").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0DE9A7"));
        pieChartView4.setPieChartData(pieChartData4);
        consumed_o.setText(String.valueOf(APPLIED_LEAVE_NUMBER_OTHER));
        left_o.setText(String.valueOf(other_available));
    }


    public void hback(View view) {
        finish();
    }
}
