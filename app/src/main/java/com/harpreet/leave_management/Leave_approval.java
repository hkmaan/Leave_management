package com.harpreet.leave_management;

import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Leave_approval extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    List<Pending_teamlist> pending_teamlist;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval);
        tabLayout= findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Pending(), "Pending");
        adapter.addFragment(new Approved(), "Approved");
        adapter.addFragment(new Rejected(), "Rejected");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        pending_teamlist=new ArrayList<>();
        database= FirebaseDatabase.getInstance();
        fetchdatafromfirebase();

    }
    public void get_data(View view){
        TextView Employee_nametxt=findViewById(R.id.name_txt);
        TextView Employee_designationtxt=findViewById(R.id.emp_degination);
        TextView Leave_typetxt=findViewById(R.id.leave_txt);

        String Employee_name=Employee_nametxt.getText().toString();
        String Employee_designation=Employee_designationtxt.getText().toString();
        String Leave_type=Leave_typetxt.getText().toString();
    }

    private void fetchdatafromfirebase() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();

    }


    public void bback(View view) {
        finish();
    }
}
