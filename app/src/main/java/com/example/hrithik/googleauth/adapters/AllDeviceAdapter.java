package com.example.hrithik.googleauth.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrithik.googleauth.activity.BulbActivity;
import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.activity.SwitchBoardActivity;

public class AllDeviceAdapter extends RecyclerView.Adapter<AllDeviceAdapter.MyViewholder> {


    private  String[] deviceName;
    private String[] deviceType;
    private String[] deviceId;


    public AllDeviceAdapter(List<String> deviceName, List<String> deviceType, List<String> deviceId){
        this.deviceName= deviceName.toArray(new String[0]);
        this.deviceType= deviceType.toArray(new String[0]);
        this.deviceId=deviceId.toArray(new String[0]);

            Log.i("result",deviceName.toArray(new String[0])+" and "+deviceType.toArray(new String[0]));
    }


    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.devices,parent,false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, final int position) {

        String str1= deviceName[position];
        String str2= deviceType[position];
        holder.deviceName.setText(str1);
        holder.deviceType.setText(str2);

        if(deviceType[position].equals("Switch Board")) {
            holder.deviceImage.setImageResource(R.drawable.board_red);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent;
                Toast.makeText(v.getContext(),deviceId[position],Toast.LENGTH_SHORT).show();
                if (deviceType[position].equals("Switch Board")) {
                     mIntent= new Intent(v.getContext(), SwitchBoardActivity.class);
                    mIntent.putExtra("DeviceId",deviceId[position]);
                    mIntent.putExtra("DeviceName",deviceName[position]);
                    mIntent.putExtra("DeviceType",deviceType[position]);
                }
                else {
                    mIntent= new Intent(v.getContext(), BulbActivity.class);
                    mIntent.putExtra("DeviceId",deviceId[position]);
                    mIntent.putExtra("DeviceName",deviceName[position]);
                    mIntent.putExtra("DeviceType",deviceType[position]);
                }
                v.getContext().startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return deviceName.length;
    }


    public class MyViewholder extends RecyclerView.ViewHolder  {
        TextView deviceName;
        TextView deviceType;
        CardView cardView;
        ImageView deviceImage;

        public MyViewholder(View itemView ) {
            super(itemView);

            cardView=itemView.findViewById(R.id.deviceHolderCardView);
            deviceType= itemView.findViewById(R.id.deviceId);
            deviceName= itemView.findViewById(R.id.deviceName);
            deviceImage=itemView.findViewById(R.id.deviceImage);

        }


    }


}