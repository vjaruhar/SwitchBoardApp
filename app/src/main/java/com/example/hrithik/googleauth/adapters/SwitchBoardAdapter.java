package com.example.hrithik.googleauth.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.utilities.PreferenceUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class SwitchBoardAdapter extends RecyclerView.Adapter<SwitchBoardAdapter.MyViewHolder> {
    private int numOfSwitches;
    private String deviceId;
    private String[] switchStatus;
    private String[] switchName;
    JSONObject switchNameObject;
    boolean isCustomSwitchNamesAvailable = false;
    private Context context;
    PreferenceUtility preferenceUtility;



    public SwitchBoardAdapter(int numOfSwitches, List<String> switchstatus, List<String> switchname, String DeviceId, JSONObject switchNameObject, Context context) {
        this.numOfSwitches = numOfSwitches;
        this.deviceId = DeviceId;
        this.switchName = switchname.toArray(new String[0]);
        this.switchStatus = switchstatus.toArray(new String[0]);
        this.context = context;
        this.preferenceUtility = new PreferenceUtility(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gridview_switch, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim())
                .child("Switches").child(switchName[position]);

        switchNameObject = preferenceUtility.getSwitchBoardSwitchNames(deviceId);
        if(this.switchNameObject != null){
            isCustomSwitchNamesAvailable = true;
        }

        if(isCustomSwitchNamesAvailable){
            if(switchNameObject.has(switchName[position])){
                try {
                    holder.textView.setText(switchNameObject.getString(switchName[position]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                holder.textView.setText("Switch " + (position + 1));
            }
        }else{
            holder.textView.setText("Switch " + (position + 1));
        }



        if (switchStatus[position].equals("1")) {
            holder.imageView.setBackgroundResource(R.drawable.ring_shape_green);
            holder.imageView.setImageResource(R.drawable.green_button_24dp);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.ring_shape);
            holder.imageView.setImageResource(R.drawable.red_button_24dp);
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchStatus[position].equals("1")) {

                    databaseReference.setValue("0")
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(holder.itemView.getContext(), "Switch " + (position + 1) + ":OFF", Toast.LENGTH_LONG).show();
                                     /*   holder.imageView.setBackgroundResource(R.drawable.ring_shape);
                                        holder.imageView.setImageResource(R.drawable.red_button_24dp);
                                        switchStatus[position] = "0"; */
                                    }
                                }
                            });


                } else {
                    databaseReference.setValue("1")
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(holder.itemView.getContext(), "Switch " + (position + 1) + ":ON", Toast.LENGTH_LONG).show();
                                        /*switchStatus[position] = "1";
                                        holder.imageView.setBackgroundResource(R.drawable.ring_shape_green);
                                        holder.imageView.setImageResource(R.drawable.green_button_24dp);*/
                                    }
                                }
                            });
                }
            }
        });

        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View alertView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_for_renaming_switch, null);
                final TextInputEditText switchNameEditText = alertView.findViewById(R.id.newSwitchNameET);
                final TextInputLayout switchNameTextLayout = alertView.findViewById(R.id.newSwitchNameTL);
                Button confirmButton = alertView.findViewById(R.id.switchNameConfirm);

                builder.setView(alertView);

                final AlertDialog alertDialog = builder.create();

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = Objects.requireNonNull(switchNameEditText.getText()).toString();
                        boolean isError = true;
                        if(TextUtils.isEmpty(name)){
                            switchNameTextLayout.setErrorEnabled(true);
                            switchNameTextLayout.setError("Please Enter a Name");
                            isError = true;
                        }else{
                            switchNameTextLayout.setErrorEnabled(false);
                            isError = false;
                        }
                        if(!isError){
                            preferenceUtility.saveSwitchName(deviceId, name.trim(), switchName[position]);
                            Toast.makeText(context, name.trim(), Toast.LENGTH_SHORT).show();
                            switchNameObject = preferenceUtility.getSwitchBoardSwitchNames(deviceId);
                            notifyDataSetChanged();
                            alertDialog.cancel();
                        }
                    }
                });

                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return numOfSwitches;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.switchIv);
            textView = itemView.findViewById(R.id.switchTv);
        }
    }
}
