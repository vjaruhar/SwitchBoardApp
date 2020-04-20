package com.example.hrithik.googleauth;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class adapterForUser extends RecyclerView.Adapter<adapterForUser.MyViewholder> {


    private  String[] name;
    private  String[] uid;
    private String deviceId;

    public  adapterForUser(List<String> name,List<String> uid,String deviceId){
        this.name= name.toArray(new String[0]);
        this.uid= uid.toArray(new String[0]);
        this.deviceId=deviceId;
    }


    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.userslist,parent,false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, final int position) {

        String str1= name[position];
        holder.name.setText(str1);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Log.i("result_uid_clicked",uid[position]);
                DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("Device_To_User").child(deviceId);
                final DatabaseReference myRef2= FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(uid[position]).child("Devices");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Log.i("getKey: ",dataSnapshot1.child("Uid").getValue().toString());
                            if (dataSnapshot1.child("Uid").getValue().toString().equals(uid[position])){
                                dataSnapshot1.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                    if (dataSnapshot1.child("DeviceId").getValue().toString().equals(deviceId)){
                                                        dataSnapshot1.getRef().removeValue();
                                                        break;
                                                    }}
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.length;
    }


    public class MyViewholder extends RecyclerView.ViewHolder  {
        TextView name;
        Button button;

        public MyViewholder(View itemView ) {
            super(itemView);

            name= itemView.findViewById(R.id.textViewName);
            button=itemView.findViewById(R.id.cancelButton);


        }


    }


}