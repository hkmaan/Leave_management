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

public class Rejected extends Fragment {
    List<Leave_DataModel> rejected_list;
    RecyclerView rejected_recycler;

    public Rejected() {
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
        return inflater.inflate(R.layout.activity_rejected_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


            rejected_list=new ArrayList<>();
        get_data_firebase();

        rejected_recycler = view.findViewById(R.id.rejected);


        rejected_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));




    }
    private void get_data_firebase() {
       FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("leaves");



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rejected_list.clear();

                for( DataSnapshot snapshot : dataSnapshot.getChildren() )
                {

                    for (DataSnapshot snapshot1:snapshot.getChildren()) {

                        Leave_DataModel data = snapshot1.getValue(Leave_DataModel.class);
                        if (data.status.equalsIgnoreCase("rejected")) {
                            data.l_id = snapshot.getKey();
                            rejected_list.add(data);
                        }
                    }
                }
                ListAdapter adapter = new ListAdapter();
                rejected_recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private class ListHolder extends RecyclerView.ViewHolder {

        TextView name_txt, designation, leave_txt, reject;
        CircleImageView img;
        RelativeLayout rejected_view;


        public ListHolder(View itemView) {
            super(itemView);

            name_txt = itemView.findViewById(R.id.name_txt1);
            designation = itemView.findViewById(R.id.emp_degination1);
            leave_txt = itemView.findViewById(R.id.leave_txt1);
            reject=itemView.findViewById(R.id.r1);
            img=itemView.findViewById(R.id.pic1);
            rejected_view=itemView.findViewById(R.id.reject_view);


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

            View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_rejected_listcell, vi, false);


            ListHolder holder = new ListHolder(view);

            return holder;


        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {

            final Leave_DataModel dataLeaveModel = rejected_list.get(i);
            listHolder.name_txt.setText(dataLeaveModel.e_name);
            listHolder.leave_txt.setText(dataLeaveModel.Leave_type);
            listHolder.designation.setText(dataLeaveModel.e_designation);
            listHolder.reject.setText(dataLeaveModel.status);
            get_image(dataLeaveModel.email,listHolder.img);

            listHolder.rejected_view.setOnClickListener(new View.OnClickListener() {
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

        }

        @Override
        public int getItemCount() {

            return rejected_list.size();
        }
    }
    private void get_image(String email, final CircleImageView image) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("employees").child(email.replace(".", ""));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("image").getValue()!=null) {
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