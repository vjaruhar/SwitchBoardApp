package com.example.hrithik.googleauth.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.adapters.AdditionalUserAdapter;
import com.example.hrithik.googleauth.adapters.SwitchBoardAdapter;
import com.example.hrithik.googleauth.utilities.PreferenceUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sdsmdg.harjot.crollerTest.Croller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwitchBoardActivity extends AppCompatActivity {
    final List<String> nameList=new ArrayList<>();
    final List<String> uidList=new ArrayList<>();
    private FirebaseAuth mAuth;
    String belongsTo;
    DatabaseReference databaseReference;
    String deviceId;
    String deviceName;
    String deviceType;
    //list of status of the switches in that deviceID
    List<String> status =new ArrayList<>();
    List<String> switchName =new ArrayList<>();
    JSONObject switchNameObject;
    PreferenceUtility preferenceUtility;
    private CardView speedC, dimmerC;
    private Croller speedCroller, dimmerCroller;
    boolean isCustomSwitchNamesAvailable = false;
    boolean isSpeedControlAvailable = false;
    boolean isDimmerAvailable = false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_switch_board);

        mAuth = FirebaseAuth.getInstance();
        //grid layout for switch boards
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(SwitchBoardActivity.this,2);
        final RecyclerView recyclerView=findViewById(R.id.recyclerView1);

        speedC = findViewById(R.id.SpeedCrollerCardView);
        dimmerC = findViewById(R.id.dimmerCardView);
        speedCroller = findViewById(R.id.SpeedCroller);
        dimmerCroller = findViewById(R.id.DimmerCroller);

        //setting that grid layout on the recycler view
        recyclerView.setLayoutManager(gridLayoutManager);

        // accepting the deviceId,deviceName, deviceType that are passed
        Intent mIntent=getIntent();
        deviceId =mIntent.getStringExtra("DeviceId");
        deviceName=mIntent.getStringExtra("DeviceName");
        deviceType=mIntent.getStringExtra("DeviceType");

        databaseReference= FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim())
                .child("Switches");
        final DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim());

        deviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Contains")){
                    for(DataSnapshot devicesTypes : dataSnapshot.child("Contains").getChildren()){
                       if(devicesTypes.getValue().equals("Speed Control")){
                           isSpeedControlAvailable = true;
                           speedC.setVisibility(View.VISIBLE);

                           int val = (int) dataSnapshot.child("speedControlValue").getValue(Integer.class);
                           speedCroller.setProgress(val);
                       }
                        if(devicesTypes.getValue().equals("Dimmer")){
                            isDimmerAvailable = true;
                            dimmerC.setVisibility(View.VISIBLE);

                            int val = (int) dataSnapshot.child("dimmerValue").getValue(Integer.class);
                            dimmerCroller.setProgress(val);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Contains")){
                    for(DataSnapshot devicesTypes : dataSnapshot.child("Contains").getChildren()){
                        if(devicesTypes.getValue().equals("Speed Control")){

                            int val = (int) dataSnapshot.child("speedControlValue").getValue(Integer.class);
                            speedCroller.setProgress(val);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        preferenceUtility = new PreferenceUtility(SwitchBoardActivity.this);

        switchNameObject = preferenceUtility.getSwitchBoardSwitchNames(deviceId);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Parcelable recyclerViewPosition=recyclerView.getLayoutManager().onSaveInstanceState();
                status.clear();
                switchName.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    status.add(dataSnapshot1.getValue().toString());
                    switchName.add(dataSnapshot1.getKey());
                }
                recyclerView.setAdapter(new SwitchBoardAdapter(status.size(),status,switchName,deviceId, switchNameObject, SwitchBoardActivity.this));
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("result",databaseError.getMessage());
            }

        });

        DatabaseReference myRefUid=FirebaseDatabase.getInstance().getReference().child("All_Devices");
        myRefUid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            belongsTo=dataSnapshot.child(deviceId).child("BelongsTo").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        speedCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                DatabaseReference speedRef = FirebaseDatabase.getInstance().getReference("All_Devices")
                        .child(deviceId.trim()).child("speedControlValue");
                speedRef.setValue(progress);
            }
        });

        dimmerCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                DatabaseReference speedRef = FirebaseDatabase.getInstance().getReference("All_Devices")
                        .child(deviceId.trim()).child("dimmerValue");
                speedRef.setValue(progress);
            }
        });





        ////////////////////////////////////////////////////////



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_device, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deviceInfo) {
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(SwitchBoardActivity.this);
            View mView=getLayoutInflater().inflate(R.layout.alertdialog_for_deviceinfo,null);
            TextView deviceNameTv=mView.findViewById(R.id.deviceName);
            TextView deviceIdTv=mView.findViewById(R.id.deviceId);


            deviceNameTv.setText(deviceName);
            deviceIdTv.setText(deviceId);

            try
            {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder
                        .encodeBitmap(
                                "{\"deviceId\":\""+deviceId+"\",\"deviceName\":\""+deviceName+"\",\"deviceType\":\""+deviceType+"\"}"
                                , BarcodeFormat.QR_CODE, 400, 400);
                ImageView qrCodeIv=mView.findViewById(R.id.qrCode);
                qrCodeIv.setImageBitmap(bitmap);
            }
            catch(Exception e)
            {
                e.getStackTrace();
            }
            //show Info about other users only to the owner of that Device:
           if(belongsTo.equals(mAuth.getUid())) {
               final RecyclerView list = mView.findViewById(R.id.recyclerViewForUser);
               list.setLayoutManager(new LinearLayoutManager(mView.getContext()));

               final DatabaseReference myRefUser = FirebaseDatabase.getInstance().getReference().child("Users");

               DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("Device_To_User").child(deviceId);

               myRef.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       nameList.clear();
                       uidList.clear();
                       for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                           if(!belongsTo.equals(dataSnapshot1.child("Uid").getValue().toString())) {
                               uidList.add(dataSnapshot1.child("Uid").getValue().toString());
                               Log.i("result_uidList: ", dataSnapshot1.child("Uid").getValue().toString());
                               final String uid=dataSnapshot1.child("Uid").getValue().toString();
                               myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           nameList.add(dataSnapshot.child(uid).child("UserInfo").child("Name").getValue().toString());
                                           Log.i("result_nameList: ", dataSnapshot.child(uid).child("UserInfo").child("Name").getValue().toString());
                                           list.setAdapter(new AdditionalUserAdapter(nameList, uidList,deviceId));
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });


                           }
                       }
                       Log.i("result_uidList_size: ", uidList.size() + " ");
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });


           }
            mBuilder.setView(mView);
            final AlertDialog alertDialog=mBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

}
