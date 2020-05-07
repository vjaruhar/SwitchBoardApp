package com.example.hrithik.googleauth.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.models.SpeedAndDimmerModel;
import com.example.hrithik.googleauth.utilities.PreferenceUtility;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.util.ArrayList;

public class SpeedAndDimmerAdapter extends RecyclerView.Adapter<SpeedAndDimmerAdapter.SpeedAndDimmerViewHolder> {

    ArrayList<SpeedAndDimmerModel> speedAndDimmerModelArrayList;
    Context context;
    private PreferenceUtility preferenceUtility;
    private String deviceId;

    public SpeedAndDimmerAdapter(ArrayList<SpeedAndDimmerModel> speedAndDimmerModelArrayList, Context context, PreferenceUtility preferenceUtility, String deviceId) {
        this.speedAndDimmerModelArrayList = speedAndDimmerModelArrayList;
        this.context = context;
        this.preferenceUtility = preferenceUtility;
        this.deviceId = deviceId;
    }

    @NonNull
    @Override
    public SpeedAndDimmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.speed_and_dimmer_recycler, parent, false);
        return new SpeedAndDimmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeedAndDimmerViewHolder holder, int position) {
        final SpeedAndDimmerModel model = speedAndDimmerModelArrayList.get(position);

        if (model.isIs_speed()){
            holder.bar.setProgress(model.getSpeedControlValue());
        }else if(model.isIs_dimmer()){
            holder.bar.setProgress(model.getDimmerValue());
        }

        holder.textView.setText(model.getDevice_no()+" is "+model.getDeviceType());
        holder.bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder crollerBuilder = new AlertDialog.Builder(context);
                View popUpView = LayoutInflater.from(context).inflate(R.layout.popup_croller_speed_dimmer, null);
                Croller popUpCroller = popUpView.findViewById(R.id.crollerPopUp);
//                Button cancelButtonPopUp = popUpView.findViewById(R.id.cancel_button_popup);
//                Button okButtonPopUp = popUpView.findViewById(R.id.ok_button_popup);

                if(model.isIs_speed()){
                    popUpCroller.setProgress((int)model.getSpeedControlValue());
                }else if(model.isIs_dimmer()){
                    popUpCroller.setProgress((int)model.getDimmerValue());
                }

                crollerBuilder.setView(popUpView);

                final AlertDialog popUpDialog = crollerBuilder.create();
                popUpDialog.setCanceledOnTouchOutside(true);
                popUpDialog.show();

                popUpCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                    @Override
                    public void onProgressChanged(int progress) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("All_Devices")
                                .child(deviceId.trim())
                                .child("Contains").child(model.getDevice_no());
                        if(model.isIs_speed()){
                            ref.child("speedControlValue").setValue(progress);
                        }else if(model.isIs_dimmer()){
                            ref.child("dimmerValue").setValue(progress);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return speedAndDimmerModelArrayList.size();
    }

    static class SpeedAndDimmerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ArcProgress bar;
         SpeedAndDimmerViewHolder(@NonNull View itemView) {
            super(itemView);
             textView = itemView.findViewById(R.id.nameTV);
             bar = itemView.findViewById(R.id.speedAndDimmerBar);
        }
    }
}
