package com.harpreet.leave_management;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class Home_employee extends AppCompatActivity {
    private TextView YearMonthTv;
    private ViewFlipper flipper;
    CircleImageView image;
    TextView name_et;
    private static ProgressDialog pd ;
    String month_name;
    List<holiday_dataModel> holiday_list;
    ExpCalendarView calendarView ;

    int APPLIED_LEAVE_NUMBER_SICK = 0;
    int APPLIED_LEAVE_NUMBER_CASUAL = 0;
    int APPLIED_LEAVE_NUMBER_PAID = 0;
    int APPLIED_LEAVE_NUMBER_OTHER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employee);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
        pie_leaveHistory();
        pd.show();
        flipper = findViewById(R.id.flipper);
        get_profile_data();
        holiday_list = new ArrayList<>();
        //nav bar
        image=findViewById(R.id.e_img);
        name_et=findViewById(R.id.e_name);
        Home_employee.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Calendar
        calendarView =  findViewById(R.id.calendarView);

        YearMonthTv = (TextView) findViewById(R.id.month_year);
       month_name = getmonthname((Calendar.getInstance().get(Calendar.MONTH) + 1));
        YearMonthTv.setText(Calendar.getInstance().get(Calendar.YEAR) + " " + month_name + " ");

        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                month_name = getmonthname((month));
                YearMonthTv.setText(String.format("%d %s", year, month_name));
                pd.show();
                get_holidays_database();
            }
        });


    }


    public void Navigationbar(View view) {
        DrawerLayout drawerLayout =findViewById(R.id.e_drawer);
        drawerLayout.openDrawer(GravityCompat.START);
    }



    public void geo_attendance(View view) {

        Intent i = new Intent(Home_employee.this , Emap.class);

        startActivity(i);
    }
    public  String getmonthname(int month){
        switch (month){
            case 1 :
                return "January";

            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "empty_string";
    }

    public void ehome(View view) {
        Intent i = new Intent(Home_employee.this , Home_employee.class);

        startActivity(i);
    }

    public void emy_profile(View view) {
        Intent i = new Intent(Home_employee.this , Employee_profile.class);

        startActivity(i);
    }

    public void Requested_leave(View view) {
        Intent i=new Intent(Home_employee.this,Requested_leave.class);
        startActivity(i);
    }

    public void Leave_history(View view) {
        Intent i = new Intent(Home_employee.this ,Employee_leave_history.class);

        startActivity(i);
    }

    public void apply_for_leave(View view) {
        Intent i = new Intent(Home_employee.this,Apply_for_Leave.class);
        startActivity(i);
    }

    private void get_holidays_database()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("holiday_list").child(month_name);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.hide();

                holiday_list.clear();

                flipper.removeAllViews();

                for ( DataSnapshot  snapshot : dataSnapshot.getChildren() )
                {
                    holiday_dataModel dataModel = snapshot.getValue(holiday_dataModel.class);

                    holiday_list.add(dataModel);

                }

                for ( int i = 0 ; i < holiday_list.size() ; i++ )
                {

                    RelativeLayout relativeLayout = new RelativeLayout(Home_employee.this);

                    RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);

                    relativeLayout.setLayoutParams(layoutParams);

                    ImageView imageView = new ImageView(Home_employee.this);

                    imageView.setLayoutParams(layoutParams);

                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Glide.with(Home_employee.this).load(holiday_list.get(i).image).into(imageView);

                    relativeLayout.addView(imageView);

                    TextView holiday_text = new TextView(Home_employee.this);

                    holiday_text.setId(R.id.txtvaisakhi);

                    RelativeLayout.LayoutParams text_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    holiday_text.setLayoutParams(text_param);

                    holiday_text.setText(holiday_list.get(i).holiday);






                    RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams) holiday_text
                            .getLayoutParams();
                    rparams.addRule(RelativeLayout.CENTER_IN_PARENT);






                    holiday_text.setTextColor(Color.RED);

                    relativeLayout.addView(holiday_text , rparams);


                    flipper.addView(relativeLayout);

                    if(holiday_list.get(i).date != null)
                    {
                        int year = Integer.parseInt(holiday_list.get(i).date.split("/")[0]);
                        int month = Integer.parseInt(holiday_list.get(i).date.split("/")[1]);
                        int day = Integer.parseInt(holiday_list.get(i).date.split("/")[2]);

                        calendarView.markDate(new DateData(year,month,day).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED)));// multiple dates with this code.

                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void logout(View view) {
        final SharedPreferences.Editor sp = getSharedPreferences("employee_info" , MODE_PRIVATE).edit();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sp.clear();
                        sp.commit();

                        finish();
                        Intent i=new Intent(Home_employee.this,layout2.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();



    }
    public  void get_profile_data(){

        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String email = sp.getString("email", "");

       FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pd.hide();

               if(dataSnapshot.child("emp_name").exists()) {
                   String name = dataSnapshot.child("emp_name").getValue().toString();
                   name_et.setText(name);
               }
                if (dataSnapshot.child("image").exists()) {

                    Glide.with(Home_employee.this).load(dataSnapshot.child("image").getValue().toString()).into(image);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void settings(View view) {
        Intent intent=new Intent(Home_employee.this,Setting.class);
        startActivity(intent);
    }
    public  void pie_leaveHistory(){
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
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_SICK++;
                        }
                    }

                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Paid Leave"))
                    {
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_PAID++;
                        }
                    }
                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Casual Leave"))
                    {
                        if (dataLeaveModel.status.equalsIgnoreCase("approved")) {
                            APPLIED_LEAVE_NUMBER_CASUAL++;
                        }
                    }
                    if(dataLeaveModel.Leave_type.equalsIgnoreCase("Other Leave"))
                    {
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

    public  void setup_pie(){

        PieChartView pieChartView = findViewById(R.id.chart);
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(APPLIED_LEAVE_NUMBER_SICK, Color.BLUE));
        pieData.add(new SliceValue(APPLIED_LEAVE_NUMBER_PAID, Color.CYAN));
        pieData.add(new SliceValue(APPLIED_LEAVE_NUMBER_CASUAL, Color.GREEN));
        pieData.add(new SliceValue(APPLIED_LEAVE_NUMBER_OTHER, Color.MAGENTA));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1(String.valueOf(APPLIED_LEAVE_NUMBER_CASUAL+APPLIED_LEAVE_NUMBER_OTHER+APPLIED_LEAVE_NUMBER_PAID+APPLIED_LEAVE_NUMBER_SICK)).setCenterText1FontSize(30).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
        get_holidays_database();


    }


}
