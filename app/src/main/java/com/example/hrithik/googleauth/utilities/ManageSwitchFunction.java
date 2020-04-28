package com.example.hrithik.googleauth.utilities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ManageSwitchFunction {
    DatabaseReference deviceRef, switchFuncRef;

    public void addSwitchFunc(String deviceId, final String switchName, String switchNo, String deviceType, String has){
        deviceRef = FirebaseDatabase.getInstance().getReference("All_Devices").child(deviceId);

        deviceRef.child("is_external_device_added").setValue(1);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("deviceType", deviceType);
        map.put("has", has);
        map.put("switch_name", switchName);
        map.put("switch_no", Integer.parseInt(switchNo));
        map.put(has, 0);
        deviceRef.child("switch_func").child(switchName).setValue(map);

    }

    public void removeSwitchFunc(String deviceId, String switchName){
        deviceRef = FirebaseDatabase.getInstance().getReference("All_Devices").child(deviceId);

        deviceRef.child("switch_func").child(switchName).removeValue();

        deviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("switch_func")){
                    deviceRef.child("is_external_device_added").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
