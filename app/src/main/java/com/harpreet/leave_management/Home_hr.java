package com.harpreet.leave_management;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class Home_hr extends AppCompatActivity {
    List<Leave_DataModel> leave_list ;
    private TextView YearMonthTv;
    private ViewFlipper flipper;
    String month_name;
    private static ProgressDialog pd ;
     private CircleImageView profile_image;
      TextView Hr_name;
    ExpCalendarView calendarView;
    List<holiday_dataModel> holiday_list;
    RecyclerView leave_recycler;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_hr);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait");
       leave_list = new ArrayList<>();
        flipper = findViewById(R.id.flipper);
            profile_image=findViewById(R.id.d_image);
            Hr_name=findViewById(R.id.d_name);
            get_profile_data();
        holiday_list = new ArrayList<>();

        //calendar
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
                //Recycler
        leave_list = new ArrayList<>();
       // prepare_data();
        get_data_firebase();
        leave_recycler = findViewById(R.id.Leave_recycler);
        leave_recycler.setLayoutManager(new LinearLayoutManager(Home_hr.this , LinearLayoutManager.HORIZONTAL , false));
      /*  ListAdapter adapter = new ListAdapter();
        leave_recycler.setAdapter(adapter);*/
        pd.show();
        get_holidays_database();

    }



    public void navigationbar(View view) {
        DrawerLayout drawerLayout =findViewById(R.id.drawerlayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }


 /* private void prepare_data()
    {
        leave_list.add("Paid Leave");
        leave_list.add("casual leave");
        leave_list.add("sick leave");
        leave_list.add("casual leave");
        leave_list.add("paid leave");
        leave_list.add("casual leave");
        leave_list.add("casual leave");

    }*/

    private void get_data_firebase()
    {
        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String empid = sp.getString("emp_id", "");

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("leaves");



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leave_list.clear();

                for( DataSnapshot snapshot : dataSnapshot.getChildren() )
                {
                    for (DataSnapshot snapshot1:snapshot.getChildren()) {
                        Leave_DataModel dataLeaveModel = snapshot1.getValue(Leave_DataModel.class);


                        if (dataLeaveModel.status.equalsIgnoreCase("pending")) {
                            dataLeaveModel.l_id = snapshot1.getKey();
                            leave_list.add(dataLeaveModel);
                        }
                    }
                }
                Home_hr.ListAdapter adapter = new Home_hr.ListAdapter();
                leave_recycler.setAdapter(adapter);
               pd.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void my_profile(View view) {
        Intent i = new Intent(Home_hr.this , Hr_profile_page.class);

        startActivity(i);
    }

    public void home(View view) {
        Intent i = new Intent(Home_hr.this , Home_hr.class);

        startActivity(i);
    }

    public void Geo_markattendance(View view) {
        Intent i = new Intent(Home_hr.this , MapsActivity.class);

        startActivity(i);
    }

    public void My_team(View view) {
        Intent i = new Intent(Home_hr.this , My_team.class);

        startActivity(i);
    }

    public void Settings(View view) {
        Intent i = new Intent(Home_hr.this , Setting.class);

        startActivity(i);


    }


    public void Logout(View view) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
      firebaseAuth.signOut();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       finish();
                       Intent i = new Intent(Home_hr.this , layout2.class);
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

        //finish();

        }



    public void Leave_approval(View view) {
        Intent i = new Intent(Home_hr.this , Leave_approval.class);

        startActivity(i);
    }

    private class ListHolder extends RecyclerView.ViewHolder
    {
        CircleImageView image;
        TextView nametxt;
        TextView Leavetxt;
        LinearLayout cardView;
        Button accept,reject;
        public  ListHolder( View itemView ) {
            super(itemView);

            nametxt = itemView.findViewById(R.id.txt5);
            Leavetxt=itemView.findViewById(R.id.Leave_text);
            image=itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.cardview);
            accept=itemView.findViewById(R.id.button);
            reject=itemView.findViewById(R.id.button2);
        }


    }
    private class ListAdapter extends RecyclerView.Adapter<ListHolder>
    {




        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup vi, int i) {

            View view = LayoutInflater.from(Home_hr.this).inflate(R.layout.activity_listcell , vi , false);


            ListHolder holder = new ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {

          //  String fruit_name = leave_list.get(i);

           // listHolder.leave_txt.setText(fruit_name);

            final Leave_DataModel dataLeaveModel = leave_list.get(i);
            listHolder.nametxt.setText(dataLeaveModel.e_name);
            listHolder.Leavetxt.setText(dataLeaveModel.Leave_type);
            listHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Home_hr.this ,Leave_approval.class);
                    i.putExtra("name",dataLeaveModel.e_name);
                    i.putExtra("id",dataLeaveModel.empid);
                    i.putExtra("designation",dataLeaveModel .e_designation);
                    i.putExtra("from",dataLeaveModel.From);
                    i.putExtra("to",dataLeaveModel.To);
                    i.putExtra("Reason",dataLeaveModel.Reason);
                    startActivity(i);
                }
            });
            listHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference("leaves").child(dataLeaveModel.empid).child(dataLeaveModel.l_id).child("status").setValue("approved");

                }
            });
            listHolder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference("leaves").child(dataLeaveModel.empid).child(dataLeaveModel.l_id).child("status").setValue("rejected");
                }
            });
            get_image(dataLeaveModel.email,listHolder.image);

        }

        @Override
        public int getItemCount() {

            return leave_list.size();
        }
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


    private void get_holidays_database()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("holiday_list").child(month_name);

        reference.addValueEventListener(new ValueEventListener() {
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

                    RelativeLayout relativeLayout = new RelativeLayout(Home_hr.this);

                    RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);

                    relativeLayout.setLayoutParams(layoutParams);

                    ImageView imageView = new ImageView(Home_hr.this);

                    imageView.setLayoutParams(layoutParams);

                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Glide.with(Home_hr.this).load(holiday_list.get(i).image).into(imageView);

                    relativeLayout.addView(imageView);

                    TextView holiday_text = new TextView(Home_hr.this);

                    holiday_text.setId(R.id.txtvaisakhi);

                     RelativeLayout.LayoutParams text_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                     holiday_text.setLayoutParams(text_param);

                     holiday_text.setText(holiday_list.get(i).holiday);






                    RelativeLayout.LayoutParams rparams = (RelativeLayout.LayoutParams) holiday_text
                            .getLayoutParams();
                    rparams.addRule(RelativeLayout.CENTER_IN_PARENT);






                    holiday_text.setTextColor(Color.BLACK);

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
    private void  get_profile_data()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        FirebaseAuth auth=FirebaseAuth.getInstance();
        String email = auth.getInstance().getCurrentUser().getEmail();
        DatabaseReference reference = database.getReference("hr_profile").child(email.replace(".",""));



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("hr_name").exists())
                    {
                        String name = dataSnapshot.child("hr_name").getValue().toString();
                        Hr_name.setText(name);
                    }


                    if(dataSnapshot.child("image").exists())
                    {
                        Glide.with(Home_hr.this).load(dataSnapshot.child("image").getValue().toString()).into(profile_image);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void get_image(String email, final CircleImageView image) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("image").getValue()!=null) {
                    Glide.with(Home_hr.this).load(dataSnapshot.child("image").getValue().toString()).into(image);
                }
                else
                {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.image));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}

