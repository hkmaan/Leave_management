package com.harpreet.leave_management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.recyclerview.extensions.ListAdapter;
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

public class My_team extends AppCompatActivity {

    List<Add_emp_data> team_list ;
    RecyclerView team_recycler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        team_list = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);

       get_data_firebase();

         team_recycler = findViewById(R.id.recycler_team);


        team_recycler.setLayoutManager(new LinearLayoutManager(My_team.this , LinearLayoutManager.VERTICAL , false));

        progressBar.setVisibility(View.VISIBLE);
    }

    private void get_data_firebase()
    {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = firebaseDatabase.getReference("employees");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                team_list.clear();

                for( DataSnapshot snapshot : dataSnapshot.getChildren() )
                {
                    Add_emp_data data = snapshot.getValue(Add_emp_data.class);


                    team_list.add(data);

                }
                My_team.ListAdapter adapter = new My_team.ListAdapter();
                team_recycler.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void back(View view) {
        finish();
    }




    public void Add_Employee(View view) {
        Intent i = new Intent(My_team.this , Add_Employee.class);

        startActivity(i);
    }



    private class ListHolder extends RecyclerView.ViewHolder
    {
        CircleImageView pic;
        TextView team_txt;
        TextView designation;
        RelativeLayout cardView;

        public  ListHolder( View itemView ) {
            super(itemView);

            team_txt = itemView.findViewById(R.id.team_txt);
            designation = itemView.findViewById(R.id.designation);
            cardView=itemView.findViewById(R.id.card_view);
            pic=itemView.findViewById(R.id.pic);


        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        get_data_firebase();

    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder>
    {




        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup vi, int i) {

            View view = LayoutInflater.from(My_team.this).inflate(R.layout.activity_team_list_cell , vi , false);


            ListHolder holder = new ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {

            final Add_emp_data data = team_list.get(i);

            listHolder.team_txt.setText(data.emp_name);
            listHolder.designation.setText(data.designation);
            listHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(My_team.this , MyTeamProfile.class);
                    i.putExtra("name",data.emp_name);
                    i.putExtra("email",data.email);
                    i.putExtra("designation",data.designation);
                    i.putExtra("id",data.emp_id);
                    startActivity(i);

                }
            });
get_image(data.email,listHolder.pic);

        }

        @Override
        public int getItemCount() {

            return team_list.size();
        }
    }

    private void get_image(String email, final CircleImageView image) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("image").exists()) {

                    Glide.with(My_team.this).load(dataSnapshot.child("image").getValue().toString()).into(image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
