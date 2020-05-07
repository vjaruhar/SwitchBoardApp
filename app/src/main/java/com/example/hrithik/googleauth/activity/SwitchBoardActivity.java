package com.example.hrithik.googleauth.activity;

import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.adapters.AddDeviceToSwitchAdapter;
import com.example.hrithik.googleauth.adapters.AdditionalUserAdapter;
import com.example.hrithik.googleauth.adapters.SpeedAndDimmerAdapter;
import com.example.hrithik.googleauth.adapters.SwitchBoardAdapter;
import com.example.hrithik.googleauth.interfaces.OnGetDataListener;
import com.example.hrithik.googleauth.models.AllProductsModel;
import com.example.hrithik.googleauth.models.SpeedAndDimmerModel;
import com.example.hrithik.googleauth.models.SwitchFuncModel;
import com.example.hrithik.googleauth.models.SwitchStatusModel;
import com.example.hrithik.googleauth.utilities.PreferenceUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SwitchBoardActivity extends AppCompatActivity {
    final List<String> nameList=new ArrayList<>();
    final List<String> uidList=new ArrayList<>();
    ArrayList<SwitchStatusModel> switchStatusModelArrayList;
    ArrayList<SpeedAndDimmerModel> speedAndDimmerModelArrayList;
    private FirebaseAuth mAuth;
    String belongsTo;
    DatabaseReference databaseReference;
    String deviceId;
    String deviceName;
    String deviceType;
    JSONObject switchNameObject;
    PreferenceUtility preferenceUtility;
    private Toolbar toolbar;
    DatabaseReference devicePointer, allProductsPointer;
    private ProgressDialog progressDialog;
    ArrayList<AllProductsModel> allProductsModelArrayList = new ArrayList<>();
    private RecyclerView speedAndDimmerRecycler;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_switch_board);

        mAuth = FirebaseAuth.getInstance();
        //grid layout for switch boards
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(SwitchBoardActivity.this,2);
        final RecyclerView recyclerView=findViewById(R.id.recyclerView1);
        speedAndDimmerRecycler = findViewById(R.id.speedAndDimmerRecycler);


        //setting that grid layout on the recycler view
        recyclerView.setLayoutManager(gridLayoutManager);
        GridLayoutManager speedGridLayoutManager =new GridLayoutManager(SwitchBoardActivity.this,2);
        speedAndDimmerRecycler.setLayoutManager(speedGridLayoutManager);

        // accepting the deviceId,deviceName, deviceType that are passed
        Intent mIntent=getIntent();
        deviceId =mIntent.getStringExtra("DeviceId");
        deviceName=mIntent.getStringExtra("DeviceName");
        deviceType=mIntent.getStringExtra("DeviceType");


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(deviceName);
        setSupportActionBar(toolbar);

        preferenceUtility = new PreferenceUtility(SwitchBoardActivity.this);

        databaseReference= FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim());

        final DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim());

        allProductsPointer = FirebaseDatabase.getInstance().getReference("All_Products");
        devicePointer = FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim());

        AllProductsModel productsModel = new AllProductsModel();
        productsModel.setDeviceName("None");
        productsModel.setHas(null);
        allProductsModelArrayList.add(productsModel);
        readData(allProductsPointer, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot device: dataSnapshot.getChildren()){
                    AllProductsModel productsModel = new AllProductsModel();
                    productsModel.setDeviceName((String) device.child("DeviceName").getValue());
                    productsModel.setHas((String) device.child("has").getValue());
                    allProductsModelArrayList.add(productsModel);
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure() {
            }
        });
        speedAndDimmerModelArrayList = new ArrayList<>();
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Contains")){
                    Parcelable recyclerViewPosition=speedAndDimmerRecycler.getLayoutManager().onSaveInstanceState();
                    speedAndDimmerModelArrayList.clear();
                    for(DataSnapshot devices : dataSnapshot.child("Contains").getChildren()){
                        SpeedAndDimmerModel model = new SpeedAndDimmerModel();
                        if(devices.child("DeviceType").getValue().toString().equals("Speed Control")){
                            model.setIs_dimmer(false);
                            model.setIs_speed(true);
                            model.setDeviceType("Speed Control");
                            model.setDevice_no(devices.getKey());
                            model.setSpeedControlValue((int)devices.child("speedControlValue").getValue(Integer.class));
                        }else if(devices.child("DeviceType").getValue().toString().equals("Dimmer")){
                            model.setIs_dimmer(true);
                            model.setIs_speed(false);
                            model.setDeviceType("Dimmer");
                            model.setDevice_no(devices.getKey());
                            model.setDimmerValue((int)devices.child("dimmerValue").getValue(Integer.class));
                        }
                        speedAndDimmerModelArrayList.add(model);
                    }
                    speedAndDimmerRecycler.setAdapter(new SpeedAndDimmerAdapter(speedAndDimmerModelArrayList, SwitchBoardActivity.this, preferenceUtility, deviceId));
                    speedAndDimmerRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewPosition);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        switchNameObject = preferenceUtility.getSwitchBoardSwitchNames(deviceId);

        switchStatusModelArrayList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Parcelable recyclerViewPosition=recyclerView.getLayoutManager().onSaveInstanceState();
                switchStatusModelArrayList.clear();
                for(DataSnapshot switches: dataSnapshot.child("Switches").getChildren()){
                    SwitchStatusModel model = new SwitchStatusModel();
                    model.setSwitchName(switches.getKey());
                    if(dataSnapshot.hasChild("is_external_device_added")){
                        if(Objects.requireNonNull(dataSnapshot.child("is_external_device_added").getValue()).toString().equals("1")){
                            if(dataSnapshot.hasChild("switch_func")){
                                if(dataSnapshot.child("switch_func").hasChild(switches.getKey())){
                                    DataSnapshot extSwitch = dataSnapshot.child("switch_func").child(switches.getKey());
                                    if(extSwitch.child("deviceType").getValue().toString().equals("Speed Control")){
                                        model.setIs_switch(false);
                                        model.setIs_dimmer(false);
                                        model.setIs_speed(true);
                                        model.setSpeedValue((long)extSwitch.child("speedControlValue").getValue());
                                    }else if(extSwitch.child("deviceType").getValue().toString().equals("Dimmer")){
                                        model.setIs_switch(false);
                                        model.setIs_dimmer(true);
                                        model.setIs_speed(false);
                                        model.setDimmerValue((long)extSwitch.child("dimmerValue").getValue());
                                    }
                                }else{
                                    model.setIs_switch(true);
                                    model.setIs_dimmer(false);
                                    model.setIs_speed(false);
                                    model.setSwitchStatus(Objects.requireNonNull(switches.getValue()).toString());
                                }
                            }else{
                                model.setIs_switch(true);
                                model.setIs_dimmer(false);
                                model.setIs_speed(false);
                                model.setSwitchStatus(Objects.requireNonNull(switches.getValue()).toString());
                            }
                        }else{
                            model.setIs_switch(true);
                            model.setIs_dimmer(false);
                            model.setIs_speed(false);
                            model.setSwitchStatus(Objects.requireNonNull(switches.getValue()).toString());
                        }
                    }else{
                        model.setIs_switch(true);
                        model.setIs_dimmer(false);
                        model.setIs_speed(false);
                        model.setSwitchStatus(Objects.requireNonNull(switches.getValue()).toString());
                    }
                    switchStatusModelArrayList.add(model);
                }
                recyclerView.setAdapter(new SwitchBoardAdapter(switchStatusModelArrayList, deviceId, SwitchBoardActivity.this));
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
            showDeviceInfo();
        }else if(id == R.id.addDeviceToSwitch){
            addDeviceToSwitchDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeviceInfo(){
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

    private void addDeviceToSwitchDialog(){

        progressDialog = new ProgressDialog(SwitchBoardActivity.this);
        final ArrayList<SwitchFuncModel> switchFuncModelArrayList;

        switchFuncModelArrayList = new ArrayList<>();

        readData(devicePointer, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot switches : dataSnapshot.child("Switches").getChildren()){
                    SwitchFuncModel model = new SwitchFuncModel();
                    model.setSwitchName(switches.getKey());
                    if(dataSnapshot.hasChild("is_external_device_added")){
                        if(dataSnapshot.child("is_external_device_added").getValue().toString().equals("1")){
                            if(dataSnapshot.hasChild("switch_func")){
                                if(dataSnapshot.child("switch_func").hasChild(switches.getKey())){
                                    model.setHas_device(true);
                                    model.setExtDeviceName((String) dataSnapshot.child("switch_func").child(switches.getKey()).child("deviceType").getValue());
                                    model.setHas((String) dataSnapshot.child("switch_func").child(switches.getKey()).child("has").getValue());
                                }else{
                                    model.setHas_device(false);
                                }
                            }
                        }
                    }else{
                        model.setHas_device(false);
                    }
                    switchFuncModelArrayList.add(model);
                }
                progressDialog.cancel();
            }

            @Override
            public void onStart() {
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }

            @Override
            public void onFailure() {
                progressDialog.cancel();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.add_device_to_switch_dialog, null);
        RecyclerView alertRv = v.findViewById(R.id.switchDeviceRecyclerDialog);

        alertRv.setLayoutManager(new LinearLayoutManager(v.getContext()));
        AddDeviceToSwitchAdapter adapter = new AddDeviceToSwitchAdapter(allProductsModelArrayList, switchFuncModelArrayList, SwitchBoardActivity.this, deviceId);
        alertRv.setAdapter(adapter);

        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                listener.onFailure();
            }
        });

    }

}
