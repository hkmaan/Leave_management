package com.harpreet.leave_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pending extends Fragment {
    List<Leave_DataModel> pending_list;
    RecyclerView pending_recycler;

    public Pending() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_pending_recycler_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        pending_list = new ArrayList<>();
        get_data_firebase();

        pending_recycler = view.findViewById(R.id.pending);


        pending_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


    }

    private void get_data_firebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("leaves");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pending_list.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        Leave_DataModel data = snapshot1.getValue(Leave_DataModel.class);
                        if (data.status.equalsIgnoreCase("pending")) {
                            data.l_id = snapshot1.getKey();
                            pending_list.add(data);
                        }
                    }
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
        Button accept,reject;
        CircleImageView img;
        RelativeLayout pending_view;

        public ListHolder(View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.name_txt);
            designation = itemView.findViewById(R.id.emp_degination);
            leave_txt = itemView.findViewById(R.id.leave_txt);
            accept=itemView.findViewById(R.id.green_but);
            reject=itemView.findViewById(R.id.red_button);
            img=itemView.findViewById(R.id.pic);
            pending_view=itemView.findViewById(R.id.pending_view);



        }
    }

    @Override
    public void onResume() {
        super.onResume();
        get_data_firebase();

    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {


        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup vi, int i) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_pending_listcell, vi, false);


            ListHolder holder = new ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {

            final Leave_DataModel dataLeaveModel = pending_list.get(i);
            listHolder.name_txt.setText(dataLeaveModel.e_name);
            listHolder.leave_txt.setText(dataLeaveModel.Leave_type);
            listHolder.designation.setText(dataLeaveModel.e_designation);
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
            listHolder.pending_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), Leave_format.class);
                    i.putExtra("name", dataLeaveModel.e_name);
                    i.putExtra("id", dataLeaveModel.empid);
                    i.putExtra("designation", dataLeaveModel.e_designation);
                    i.putExtra("from", dataLeaveModel.From);
                    i.putExtra("type", dataLeaveModel.Leave_type);
                    i.putExtra("to", dataLeaveModel.To);
                    i.putExtra("Reason", dataLeaveModel.Reason);
                    startActivity(i);
                }
            });
            get_image(dataLeaveModel.email,listHolder.img);


        }

        @Override
        public int getItemCount() {

            return pending_list.size();
        }
    }
    private void get_image(String email, final CircleImageView image) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("image").exists()) {

                    Glide.with(getContext()).load(dataSnapshot.child("image").getValue().toString()).into(image);
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