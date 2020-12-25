package com.teknesya.bimacampadmin.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.teknesya.bimacampadmin.ItemLead;
import com.teknesya.bimacampadmin.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class LeadViewAdapter extends RecyclerView.Adapter<LeadViewAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private ArrayList<ItemLead> itemList;
    // Constructor of the class
    Context context;
    public LeadViewAdapter(ArrayList<ItemLead> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lead_iten, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        final ProgressDialog pbar=new ProgressDialog(context);
        pbar.setMessage("Please Wait");
        pbar.setCanceledOnTouchOutside(false);

        holder.key.setText(itemList.get(listPosition).getKey());
        holder.phone=(itemList.get(listPosition).getValue());
        holder.value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+holder.phone));
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // get position
                Toasty.success(context,"Ask Developer For the Permission").show();
//                Intent it=new Intent(context,ViewLeadDetail.class);
//                it.putExtra("nodeId",itemList.get(listPosition).getNodeId());
//                context.startActivity(it);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                return false;
            }
        });



    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView key;
        String phone="";
        public ImageView value;
        public ViewHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.key);
            value = (ImageView) itemView.findViewById(R.id.value);
        }


    }
}