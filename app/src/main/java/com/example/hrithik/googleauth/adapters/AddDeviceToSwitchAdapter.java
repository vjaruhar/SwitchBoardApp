package com.example.hrithik.googleauth.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.interfaces.OnGetDataListener;
import com.example.hrithik.googleauth.models.AllProductsModel;
import com.example.hrithik.googleauth.models.SwitchFuncModel;
import com.example.hrithik.googleauth.utilities.ManageSwitchFunction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDeviceToSwitchAdapter extends RecyclerView.Adapter<AddDeviceToSwitchAdapter.MyViewHolder> {

    private ArrayList<AllProductsModel> allProductsModels;
    private ArrayList<SwitchFuncModel> switchFuncModels;
    private Context context;
    private String deviceId;
    public AddDeviceToSwitchAdapter(final ArrayList<AllProductsModel> allProductsModels, ArrayList<SwitchFuncModel> switchFuncModels, Context context, String deviceId) {
        this.switchFuncModels = switchFuncModels;
        this.context = context;
        this.allProductsModels = allProductsModels;
        this.deviceId = deviceId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_item_for_switch_to_device, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final SwitchFuncModel model = switchFuncModels.get(position);
        final boolean[] spinnerInitialized = {false};
        holder.switchNameTv.setText(model.getSwitchName());

        ArrayAdapter<AllProductsModel> adapter = new ArrayAdapter<AllProductsModel>(context.getApplicationContext(), android.R.layout.simple_spinner_item, allProductsModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        if(model.isHas_device()){
            for(int i = 0; i < allProductsModels.size(); i++){
                AllProductsModel productModel = allProductsModels.get(i);
                if(model.getExtDeviceName().equals(productModel.getDeviceName())){
                    holder.spinner.setSelection(i);
                    break;
                }
            }
        }else{
            holder.spinner.setSelection(0);
        }

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerInitialized[0]){
                    spinnerInitialized[0] = true;
                    return;
                }
                AllProductsModel pModel = allProductsModels.get(holder.spinner.getSelectedItemPosition());
                if(holder.spinner.getSelectedItemPosition() == 0){
                    if(model.isHas_device()){
                        ManageSwitchFunction manageSwitchFunction = new ManageSwitchFunction();
                        manageSwitchFunction.removeSwitchFunc(deviceId, model.getSwitchName());
                        Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //add device
                    ManageSwitchFunction manageSwitchFunction = new ManageSwitchFunction();
                    manageSwitchFunction.addSwitchFunc(deviceId, model.getSwitchName(), String.valueOf(position+1), pModel.getDeviceName(), pModel.getHas());
                    Toast.makeText(context, "add", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return switchFuncModels.size();
    }

     static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView switchNameTv;
        Spinner spinner;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            switchNameTv = itemView.findViewById(R.id.switchNumTV);
            spinner = itemView.findViewById(R.id.deviceTypeSpinner);

        }
    }
}
