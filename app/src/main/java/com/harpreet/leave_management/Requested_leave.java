package com.harpreet.leave_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Requested_leave extends AppCompatActivity {
    List<Leave_DataModel> requested_list;
    RecyclerView requested_recycler;
    ProgressBar progressBar;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_leave);
        requested_list = new ArrayList<>();

        progressBar = findViewById(R.id.progressbar1);

        get_data_firebase();


        requested_recycler = findViewById(R.id.requested_recycler);
        requested_recycler.setLayoutManager(new LinearLayoutManager(Requested_leave.this, LinearLayoutManager.VERTICAL, false));

        progressBar.setVisibility(View.VISIBLE);
    }


    private void get_data_firebase() {
        SharedPreferences sp = getSharedPreferences("employee_info", MODE_PRIVATE);
        String empid = sp.getString("emp_id", "");

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("leaves").child(empid.replace(".", ""));


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requested_list.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Leave_DataModel dataLeaveModel = snapshot.getValue(Leave_DataModel.class);


                    requested_list.add(dataLeaveModel);

                }
                Requested_leave.ListAdapter adapter = new Requested_leave.ListAdapter();
                requested_recycler.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class ListHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView nametxt;
        TextView designation_txt;
        TextView type_txt, status;
        RelativeLayout cardView;


        public ListHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.request_pic);
            nametxt = itemView.findViewById(R.id.name_txt);
            designation_txt = itemView.findViewById(R.id.emp_designation);
            type_txt = itemView.findViewById(R.id.leave_txtt);
            cardView = itemView.findViewById(R.id.requested_cardview);
            status = itemView.findViewById(R.id.status);
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {


        @NonNull
        @Override
        public Requested_leave.ListHolder onCreateViewHolder(@NonNull ViewGroup vi, int i) {

            View view = LayoutInflater.from(Requested_leave.this).inflate(R.layout.activity_listcell_requestedleave, vi, false);


            ListHolder holder = new ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {
            final Leave_DataModel data = requested_list.get(i);
            listHolder.nametxt.setText(data.e_name);
            listHolder.designation_txt.setText(data.e_designation);
            listHolder.type_txt.setText(data.Leave_type);
            if (data.status.equalsIgnoreCase("pending")) {
                listHolder.status.setText("Pending");
            }
            else if(data.status.equalsIgnoreCase("approved")) {
                listHolder.status.setText("Approved");
            }
            else
                {
                listHolder.status.setText("Rejected");
                listHolder.status.setTextColor(getResources().getColor(R.color.red));
            }
            listHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Requested_leave.this, Leave_format.class);
                    i.putExtra("name", data.e_name);
                    i.putExtra("id", data.empid);
                    i.putExtra("designation", data.e_designation);
                    i.putExtra("from", data.From);
                    i.putExtra("type", data.Leave_type);
                    i.putExtra("to", data.To);
                    i.putExtra("Reason", data.Reason);
                    startActivity(i);
                }
            });
            get_image(data.email,listHolder.image);


        }

        @Override
        public int getItemCount() {

            return requested_list.size();
        }
    }

    private void get_image(String email, final CircleImageView image) {

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("image").exists()) {

                    Glide.with(Requested_leave.this).load(dataSnapshot.child("image").getValue().toString()).into(image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void req_back(View view) {
        finish();
    }
}
