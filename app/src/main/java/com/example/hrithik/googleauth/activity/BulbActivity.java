package com.example.hrithik.googleauth.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrithik.googleauth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

public class BulbActivity extends AppCompatActivity {
    ImageView colorPalette;
    ImageView colorView;
    int rseek=0,gseek=0,bseek=0;
    String deviceId;
    GradientDrawable gd;
    SeekBar redSeekBar;
    SeekBar greenSeekBar;
    SeekBar blueSeekBar;
    String deviceName;
    String deviceType;
    Bitmap bitmap;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulb);
        redSeekBar=findViewById(R.id.redSeekbar);
        greenSeekBar=findViewById(R.id.greenSeekbar);
        blueSeekBar=findViewById(R.id.blueSeekbar);
        Button RedColorButton=findViewById(R.id.redColorButton);
        final Button GreenColorButton=findViewById(R.id.greenColorButton);
        Button BlueColorButton=findViewById(R.id.blueColorButton);
        Button YellowColorButton=findViewById(R.id.yellowColorButton);
        final ImageView bulbSwitch=findViewById(R.id.bulbSwitch);
        Intent mIntent=getIntent();
        deviceId =mIntent.getStringExtra("DeviceId");
        deviceName=mIntent.getStringExtra("DeviceName");
        deviceType=mIntent.getStringExtra("DeviceType");

        Toast.makeText(this,deviceId,Toast.LENGTH_SHORT).show();



        databaseReference= FirebaseDatabase.getInstance().getReference("All_Devices")
                .child(deviceId.trim())
                .child("Color_Palette");
        colorPalette=findViewById(R.id.colorPalette);
       colorView=findViewById(R.id.viewColor);

       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               long r= (long) dataSnapshot.child("r").getValue();
               long g= (long) dataSnapshot.child("g").getValue();
               long b= (long) dataSnapshot.child("b").getValue();
               /*GradientDrawable gd=(GradientDrawable)colorView.getBackground();
               gd.setColor(Color.rgb(r,g,b));*/
               redSeekBar.setProgress((int) (r*100)/255);
               greenSeekBar.setProgress((int)(g*100)/255);
               blueSeekBar.setProgress((int)(b*100)/255);
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Log.e("result",databaseError.getMessage());
           }
       });

       redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
               Map myMap=new HashMap<>();
               rseek=(progress*255)/100;
               myMap.put("r",(progress*255)/100);
               databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                   @Override
                   public void onComplete(@NonNull Task task) {
                       if (task.isSuccessful())
                       {
                           bulbSwitch.setBackgroundResource(R.drawable.ring_shape_green);
                           bulbSwitch.setImageResource(R.drawable.green_button_24dp);
                           GradientDrawable gd=(GradientDrawable)colorView.getBackground();
                           gd.setColor(Color.rgb(rseek,gseek,bseek));
                       }
                   }
               });
           }
           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                Map myMap=new HashMap<>();
                gseek=(progress*255)/100;
                myMap.put("g",(progress*255)/100);
                databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            bulbSwitch.setBackgroundResource(R.drawable.ring_shape_green);
                            bulbSwitch.setImageResource(R.drawable.green_button_24dp);
                            GradientDrawable gd=(GradientDrawable)colorView.getBackground();
                            gd.setColor(Color.rgb(rseek,gseek,bseek));
                        }
                    }
                });
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                Map myMap=new HashMap<>();
                bseek=(progress*255)/100;
                myMap.put("b",(progress*255)/100);
                databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            bulbSwitch.setBackgroundResource(R.drawable.ring_shape_green);
                            bulbSwitch.setImageResource(R.drawable.green_button_24dp);
                            GradientDrawable gd=(GradientDrawable)colorView.getBackground();
                            gd.setColor(Color.rgb(rseek,gseek,bseek));
                        }
                    }
                });
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RedColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map myMap=new HashMap<>();
                myMap.put("r",255);
                myMap.put("g",0);
                myMap.put("b",0);
                databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            bulbSwitch.setBackgroundResource(R.drawable.ring_shape_green);
                            bulbSwitch.setImageResource(R.drawable.green_button_24dp);
                            redSeekBar.setProgress(100);
                            blueSeekBar.setProgress(0);
                            greenSeekBar.setProgress(0);
                        }
                    }
                });
            }
        });

        GreenColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map myMap=new HashMap<>();
                myMap.put("r",0);
                myMap.put("g",255);
                myMap.put("b",0);
                databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            redSeekBar.setProgress(0);
                            blueSeekBar.setProgress(0);
                            greenSeekBar.setProgress(100);
                        }
                    }
                });
            }
        });

        YellowColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map myMap=new HashMap<>();
                myMap.put("r",255);
                myMap.put("g",255);
                myMap.put("b",0);
                databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            redSeekBar.setProgress(100);
                            blueSeekBar.setProgress(0);
                            greenSeekBar.setProgress(100);
                        }
                    }
                });
            }
        });
        BlueColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map myMap=new HashMap<>();
                myMap.put("r",0);
                myMap.put("g",0);
                myMap.put("b",255);
                databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            redSeekBar.setProgress(0);
                            blueSeekBar.setProgress(100);
                            greenSeekBar.setProgress(0);
                        }
                    }
                });
            }
        });

        colorPalette.setDrawingCacheEnabled(true);
        colorPalette.buildDrawingCache(true);
        colorPalette.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if((event.getAction()==MotionEvent.ACTION_MOVE||event.getAction()==MotionEvent.ACTION_DOWN)
                        &&event.getAction()!=MotionEvent.ACTION_OUTSIDE){
                    if (v==null)
                        return false;
                    bitmap=colorPalette.getDrawingCache();
                    int pixel;
                    if(((int)event.getY()<bitmap.getHeight()&&(int)event.getY()>0)&&
                            ((int)event.getX()<bitmap.getWidth()&&(int)event.getX()>0)){
                     pixel=bitmap.getPixel((int)event.getX(),(int)event.getY());
                     if (pixel==Color.TRANSPARENT)
                         return false;
                    }
                  else {
                        return false;
                    }
                    //getting RGB value:
                    final int r= Color.red(pixel);
                    final int g= Color.green(pixel);
                    final int b= Color.blue(pixel);

                    Map myMap=new HashMap<>();
                    myMap.put("r",r);
                    myMap.put("g",g);
                    myMap.put("b",b);
                    databaseReference.updateChildren(myMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                           if (task.isSuccessful()){
                               redSeekBar.setProgress((r*100)/255);
                               blueSeekBar.setProgress((g*100)/255);
                               greenSeekBar.setProgress((b*100)/255);
                               bulbSwitch.setBackgroundResource(R.drawable.ring_shape_green);
                               bulbSwitch.setImageResource(R.drawable.green_button_24dp);
                           }
                        }
                    });


                }
                return true;
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
        if (id == R.id.deviceInfo)
        {
            AlertDialog.Builder mBuilder=new AlertDialog.Builder(BulbActivity.this);
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
            mBuilder.setView(mView);
            final AlertDialog alertDialog=mBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
