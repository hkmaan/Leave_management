package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pending_recyclerView extends AppCompatActivity {
    List<Leave_DataModel> pending_list;
    RecyclerView pending_recycler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_recycler_view);
        pending_list = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);

        get_data_firebase();

        pending_recycler = findViewById(R.id.pending);


        pending_recycler.setLayoutManager(new LinearLayoutManager(Pending_recyclerView.this, LinearLayoutManager.VERTICAL, false));

        progressBar.setVisibility(View.VISIBLE);
    }

    private void get_data_firebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("leaves");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pending_list.clear();
                progressBar.setVisibility(View.INVISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Leave_DataModel data = snapshot.getValue(Leave_DataModel.class);
                    data.l_id = snapshot.getKey();
                    pending_list.add(data);
                }
                ListAdapter adapter = new ListAdapter();
                pending_recycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private class ListHolder extends RecyclerView.ViewHolder {

        TextView name_txt, designation, leave_txt;
        Button accept, reject;


        public ListHolder(View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.name_txt);
            designation = itemView.findViewById(R.id.emp_designation);
            leave_txt = itemView.findViewById(R.id.leave_txt);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_data_firebase();

    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {


        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup vi, int i) {

            View view = LayoutInflater.from(Pending_recyclerView.this).inflate(R.layout.activity_team_list_cell, vi, false);


            ListHolder holder = new ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {

            final Leave_DataModel data = pending_list.get(i);

            listHolder.name_txt.setText(data.e_name);
            listHolder.designation.setText(data.e_designation);
            listHolder.leave_txt.setText(data.Leave_type);
            listHolder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            listHolder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

        @Override
        public int getItemCount() {

            return pending_list.size();
        }
    }
}

