package com.example.hrithik.googleauth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class profileActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference myRef;
    List<String> deviceName=new ArrayList<>();
    List<String> deviceType=new ArrayList<>();
    List<String> deviceId=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //adapter
        final RecyclerView list = findViewById(R.id.recyclerView);
        list.setLayoutManager(new LinearLayoutManager(this));


        //Retrieving data for users recyclerView:-
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference myRef1= FirebaseDatabase.getInstance().getReference().child("All_Devices");
        final DatabaseReference myRef_forUser_devices=FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mAuth.getUid()).child("Devices");
        Log.i("result","In profileActivity");

        //ValueEventListener to add devices in list to reflect it on recyclerView

        myRef_forUser_devices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot dataSnapshot0:dataSnapshot.getChildren())
                {

                    myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                                if (dataSnapshot1.getKey().equals(dataSnapshot0.child("DeviceId").getValue())) {

                                    //adding device_name and device_type in the list that is passed to adapter
                                    if (!deviceName.contains(dataSnapshot1.child("DeviceName").getValue().toString()))
                                    {
                                        deviceName.add(dataSnapshot1.child("DeviceName").getValue().toString());
                                        deviceType.add(dataSnapshot1.child("DeviceType").getValue().toString());
                                        deviceId.add(dataSnapshot1.child("DeviceId").getValue().toString());
                                    }
                                }
                            }
                            //passing the device_info(list) to adapter to show devices on the recyclerView
                            list.setAdapter(new MyAdapter(deviceName,deviceType,deviceId));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("result",databaseError.getDetails());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("result",databaseError.getDetails());
            }});




        //floating button to add devices
        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog is set when FAB  is pressed
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(profileActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.alertdialog,null);
                final EditText deviceId=mView.findViewById(R.id.deviceId);
                final EditText deviceName=mView.findViewById(R.id.deviceName);
                TextView scanQR=mView.findViewById(R.id.scanQR);
                SpannableString spannableString=new SpannableString(scanQR.getText());
                Button cancelBtn=mView.findViewById(R.id.cancelBtn);
                Button okBtn=mView.findViewById(R.id.okBtn);

                 //setting view to the AlertDialog
                mBuilder.setView(mView);
                final AlertDialog alertDialog=mBuilder.create();

                alertDialog.setCanceledOnTouchOutside(false);

                //OnClickListeners of cancel and ok buttons
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String DeviceId= deviceId.getText().toString().trim();
                        final String DeviceName=deviceName.getText().toString().trim();
                        if(!DeviceId.equals("")&&!DeviceName.equals(""))
                        {
                            DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference("Users")
                                    .child(mAuth.getUid())
                                    .child("Devices");
                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int i=0;
                                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                                        if(dataSnapshot1.child("DeviceId").getValue().equals(DeviceId)){
                                           break;
                                        }
                                        else
                                            i++;
                                    }
                                    if(i>=dataSnapshot.getChildrenCount())
                                           addDevice(DeviceName,DeviceId);
                                    else
                                        Toast.makeText(profileActivity.this,"This Device has already been added to your account!",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                 Log.i("result:",databaseError.getMessage());
                                }
                            });
                            alertDialog.dismiss();
                        }
                        else
                            if (DeviceId.equals("")&&DeviceName.equals("")) {
                                deviceId.setError("Please enter the DeviceID!");
                                deviceName.setError("Please enter the DeviceName!");
                            }
                            else
                                if(DeviceId.equals(""))
                                    deviceId.setError("Please enter the DeviceID!");
                                else
                                    deviceName.setError("Please enter the DeviceName!");
                    }
                });
                 //OR code Scanner :
                ClickableSpan clickableSpan =new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        IntentIntegrator intentIntegrator=new IntentIntegrator(profileActivity.this);
                        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                        intentIntegrator.setPrompt("SCAN");
                        intentIntegrator.setOrientationLocked(false);
                        intentIntegrator.setCameraId(0);
                        intentIntegrator.setBeepEnabled(false);
                        intentIntegrator.setBarcodeImageEnabled(false);
                        alertDialog.dismiss();
                        intentIntegrator.initiateScan();

                    }
                };
                spannableString.setSpan(clickableSpan,0,scanQR.getText().length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                scanQR.setText(spannableString);
                scanQR.setMovementMethod(LinkMovementMethod.getInstance());
                alertDialog.show();
            }
        });
    }
     //revoke access to remove the saved google signIn account when user press logout
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(profileActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getApplicationContext(), pageSignIn.class);
                        startActivity(myIntent);
                        finish();
                    }
                });
    }

    //onActivityResult to get result from the Scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result =IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(profileActivity.this,"Not Scanned Properly!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    JSONObject jsonObject=new JSONObject(result.getContents());
                    final String DeviceId=jsonObject.getString("deviceId");

                    DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference("Users")
                            .child(mAuth.getUid())
                            .child("Devices");
                    myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int i=0;
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                if(dataSnapshot1.child("DeviceId").getValue().equals(DeviceId)){
                                    break;
                                }
                                else
                                    i++;
                            }
                            if(i>=dataSnapshot.getChildrenCount())
                            {
                                String uiqueId;
                                final DatabaseReference myref3=FirebaseDatabase.getInstance().getReference("Device_To_User");
                                final Map myMap_UsersDevice= new HashMap();
                                final Map nm=new HashMap();
                                nm.put("Uid",mAuth.getUid());
                                myMap_UsersDevice.put("DeviceId",DeviceId);
                                final DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference("Users")
                                        .child(mAuth.getUid())
                                        .child("Devices");
                                uiqueId =myRef1.push().getKey().trim();

                                myRef1.child(uiqueId).setValue(myMap_UsersDevice).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            myref3.child(DeviceId).push().updateChildren(nm);
                                            Toast.makeText(profileActivity.this,"Device Added!",Toast.LENGTH_LONG).show();
                                        }}
                                });
                            }
                            else
                                Toast.makeText(profileActivity.this,"This Device has already been added to your account!",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("result:",databaseError.getMessage());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(profileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //optionmenu for profile activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
  //when any option is pressed:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        int id = item.getItemId();
        if (id == R.id.Logout_option) {
            //logout button in option menu is pressed
            mAuth.getInstance().signOut();
            Log.i("result", "User Logged Out");
            revokeAccess();
        }
        else
            if (id==R.id.remove_device)
            { //remove device item in option menu is pressed
                FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference myRef1= FirebaseDatabase.getInstance().getReference().child("All_Devices");
                final DatabaseReference myRef2= FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(mAuth.getUid()).child("Devices");

                int buttons = deviceName.size();
                Toast.makeText(profileActivity.this, "remove:"+buttons, Toast.LENGTH_SHORT).show();
                //alertDialog to select the device :
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(profileActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.remove_device_alertdialog,null);

                Button cancelBtn=mView.findViewById(R.id.cancelBtn);
                Button okBtn=mView.findViewById(R.id.okBtn);
                final RadioGroup rgp =mView.findViewById(R.id.radio_group);

              //adding the radio button from the list that is used for recyclerView
                for (int i = 0; i < buttons ; i++) {
                    RadioButton rbn = new RadioButton(this);
                    rbn.setId(i);
                    rbn.setText(deviceName.get(i));
                    rgp.addView(rbn);
                }

                //setting view to the AlertDialog
                mBuilder.setView(mView);
                final AlertDialog alertDialog=mBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                //OnClickListeners of cancel and ok buttons
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rgp.getCheckedRadioButtonId()==-1)
                            Toast.makeText(profileActivity.this,"Please select a device!",
                                    Toast.LENGTH_SHORT).show();
                       else{
                            final ProgressDialog progressDialog=new ProgressDialog(profileActivity.this);
                            progressDialog.setMessage("Removing Device...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                       final int id= rgp.getCheckedRadioButtonId();
                                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                           {
                                               if(dataSnapshot1.child("DeviceId").getValue().equals(deviceId.get(id))) {
                                                   dataSnapshot1.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful()){
                                                               deviceId.remove(id);
                                                               deviceName.remove(id);
                                                               deviceType.remove(id);
                                                               Toast.makeText(profileActivity.this,"Device Removed!",
                                                                       Toast.LENGTH_SHORT).show();
                                                               alertDialog.dismiss();
                                                               progressDialog.dismiss();
                                                               RecyclerView list = findViewById(R.id.recyclerView);
                                                               list.setLayoutManager(new LinearLayoutManager(profileActivity.this));
                                                               list.setAdapter(new MyAdapter(deviceName,deviceType,deviceId));
                                                           }
                                                       }
                                                   });
                                             break;  }
                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                       }
                                   });
                    }}
                });
                alertDialog.show();

            }

        return super.onOptionsItemSelected(item);
    }

    public void addDevice(final String deviceName, final String deviceId)
    {
        final ProgressDialog progressDialog=new ProgressDialog(profileActivity.this);
        progressDialog.setMessage("Adding Device...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Log.i("result","In add device method");
        mAuth.getInstance().getCurrentUser();
            final String[] uiqueId = new String[1];
            final DatabaseReference myRef2=FirebaseDatabase.getInstance().getReference("All_Devices")
                    .child(deviceId.trim());

            final DatabaseReference myRef3=FirebaseDatabase.getInstance().getReference("Device_To_User");
        myRef=FirebaseDatabase.getInstance().getReference("All_Devices");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(final DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                  if (dataSnapshot1.getKey().equals(deviceId))
                  {
                      final Map myMap_UsersDevice= new HashMap();
                      myMap_UsersDevice.put("DeviceId",deviceId.trim());
                      final DatabaseReference myRef1=FirebaseDatabase.getInstance().getReference("Users")
                              .child(mAuth.getUid())
                              .child("Devices");
                      uiqueId[0] =myRef1.push().getKey().trim();
                      if(dataSnapshot1.child("DeviceName").getValue().equals("")
                              && dataSnapshot1.child("BelongsTo").getValue().equals(""))
                      {
                          myRef2.child("DeviceName").setValue(deviceName).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      Map nm=new HashMap();
                                      nm.put("Uid",mAuth.getUid());
                                      myRef3.child(deviceId).push().updateChildren(nm);
                                      myRef1.child(uiqueId[0]).setValue(myMap_UsersDevice).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if(task.isSuccessful()){
                                                  myRef2.child("BelongsTo").setValue(mAuth.getUid());
                                                  progressDialog.dismiss();
                                                  Toast.makeText(profileActivity.this,"Device Added!",Toast.LENGTH_LONG).show();
                                              }}
                                      });
                                  }
                              }
                          }); }
                      else {
                          progressDialog.dismiss();
                          Toast.makeText(profileActivity.this,"Scan QR Code to add existing DeviceId.",Toast.LENGTH_SHORT)
                                  .show();
                      }
                      break;
                  }
                  else
                      i++;
                }

                if(i>=dataSnapshot.getChildrenCount()){
                    progressDialog.dismiss();
                    Toast.makeText(profileActivity.this,"DeviceID not found!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                Log.i("result:",databaseError.getMessage());
            }
        });
    }
    }
