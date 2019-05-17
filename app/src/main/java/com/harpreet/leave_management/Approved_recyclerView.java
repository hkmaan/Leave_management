package com.harpreet.leave_management;

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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Approved_recyclerView extends AppCompatActivity {
    List<Leave_DataModel> approved_list;
    RecyclerView approved_recycler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_recycler_view);
        approved_list = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);

        get_data_firebase();

        approved_recycler = findViewById(R.id.pending);


        approved_recycler.setLayoutManager(new LinearLayoutManager(Approved_recyclerView.this, LinearLayoutManager.VERTICAL, false));

        progressBar.setVisibility(View.VISIBLE);
    }
    private void get_data_firebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("leaves");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                approved_list.clear();
                progressBar.setVisibility(View.INVISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Leave_DataModel data = snapshot.getValue(Leave_DataModel.class);
                    data.l_id = snapshot.getKey();
                    approved_list.add(data);
                }
                Approved_recyclerView.ListAdapter adapter = new Approved_recyclerView.ListAdapter();
                approved_recycler.setAdapter(adapter);

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

            name_txt = itemView.findViewById(R.id.name_txt2);
            designation = itemView.findViewById(R.id.emp_degination2);
            leave_txt = itemView.findViewById(R.id.leave_txt2);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_data_firebase();

    }

    private class ListAdapter extends RecyclerView.Adapter<Approved_recyclerView.ListHolder> {


        @NonNull
        @Override
        public Approved_recyclerView.ListHolder onCreateViewHolder(@NonNull ViewGroup vi, int i) {

            View view = LayoutInflater.from(Approved_recyclerView.this).inflate(R.layout.activity_approved_listcell, vi, false);


            Approved_recyclerView.ListHolder holder = new Approved_recyclerView.ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull Approved_recyclerView.ListHolder listHolder, int i) {

            final Leave_DataModel data = approved_list.get(i);




        }

        @Override
        public int getItemCount() {

            return approved_list.size();
        }
    }
}
