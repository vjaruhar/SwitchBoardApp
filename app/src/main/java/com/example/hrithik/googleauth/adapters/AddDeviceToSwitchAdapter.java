package com.example.hrithik.googleauth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrithik.googleauth.R;
import com.example.hrithik.googleauth.models.AllProductsModel;
import com.example.hrithik.googleauth.models.SwitchFuncModel;

import java.util.ArrayList;

public class AddDeviceToSwitchAdapter extends RecyclerView.Adapter<AddDeviceToSwitchAdapter.MyViewHolder> {

    private ArrayList<AllProductsModel> allProductsModels;
    private ArrayList<SwitchFuncModel> switchFuncModels;
    private Context context;

    public AddDeviceToSwitchAdapter(ArrayList<AllProductsModel> allProductsModels, ArrayList<SwitchFuncModel> switchFuncModels, Context context) {
        this.allProductsModels = allProductsModels;
        this.switchFuncModels = switchFuncModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_item_for_switch_to_device, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SwitchFuncModel model = switchFuncModels.get(position);
        holder.switchNameTv.setText(model.getSwitchName());

        ArrayAdapter<AllProductsModel> adapter = new ArrayAdapter<AllProductsModel>(context, R.layout.support_simple_spinner_dropdown_item, allProductsModels);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return switchFuncModels.size();
    }

     static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView switchNameTv;
        AppCompatSpinner spinner;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            switchNameTv = itemView.findViewById(R.id.switchNumTV);
            spinner = itemView.findViewById(R.id.deviceTypeSpinner);

        }
    }
}
