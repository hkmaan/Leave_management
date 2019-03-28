package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Home_hr extends AppCompatActivity {
    List<String> leave_list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_hr);
        leave_list = new ArrayList<>();

        prepare_data();


        RecyclerView leave_recycler = findViewById(R.id.Leave_recycler);

        leave_recycler.setLayoutManager(new LinearLayoutManager(Home_hr.this , LinearLayoutManager.HORIZONTAL , false));

        ListAdapter adapter = new ListAdapter();

        leave_recycler.setAdapter(adapter);
    }


    public void navigationbar(View view) {
        DrawerLayout drawerLayout =findViewById(R.id.drawerlayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
    private void prepare_data()
    {
        leave_list.add("Paid Leave");
        leave_list.add("casual leave");
        leave_list.add("sick leave");
        leave_list.add("casual leave");
        leave_list.add("paid leave");
        leave_list.add("casual leave");
        leave_list.add("casual leave");

    }

    public void my_profile(View view) { Intent i = new Intent(Home_hr.this , Hr_profile_page.class);

        startActivity(i);
    }

    private class ListHolder extends RecyclerView.ViewHolder
    {

        TextView leave_txt;

        public  ListHolder( View itemView ) {
            super(itemView);

            leave_txt = itemView.findViewById(R.id.Leave_text);

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

            String fruit_name = leave_list.get(i);

            listHolder.leave_txt.setText(fruit_name);


        }

        @Override
        public int getItemCount() {

            return leave_list.size();
        }
    }
}

