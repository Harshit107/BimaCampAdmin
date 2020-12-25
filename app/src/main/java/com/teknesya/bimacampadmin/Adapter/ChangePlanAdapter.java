package com.teknesya.bimacampadmin.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.FirebaseDatabase;
import com.teknesya.bimacampadmin.PlanItem;
import com.teknesya.bimacampadmin.R;

import java.util.ArrayList;

public class ChangePlanAdapter extends RecyclerView.Adapter<ChangePlanAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private ArrayList<PlanItem> itemList;
    // Constructor of the class
    Context context;
    Activity activity;
    public ChangePlanAdapter(ArrayList<PlanItem> itemList, Context context, Activity activity) {
        this.itemList = itemList;
        this.context=context;
        this.activity = activity;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        final ProgressDialog pbar=new ProgressDialog(context);
        pbar.setMessage("Please Wait");
        pbar.setCanceledOnTouchOutside(false);

        holder.value.setText(itemList.get(listPosition).getValue());
//        holder.value.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:"+holder.phone));
//                context.startActivity(intent);
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // get position
//               // Toasty.success(context,itemList.get(listPosition).getNodeId()).show();
//                Intent it=new Intent(context,ViewLeadDetail.class);
//                it.putExtra("nodeId",itemList.get(listPosition).getNodeId());
//                context.startActivity(it);
//            }
//        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                 final String key = itemList.get(listPosition).getKey();
                 new AlertDialog.Builder(activity)
                         .setMessage("Do You want to remove "+itemList.get(listPosition).getValue())
                         .setTitle("Delete Plan")
                         .setCancelable(false)
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 FirebaseDatabase.getInstance().getReference().child("plan").child(key).removeValue();
                                 itemList.remove(key);
                                 //ChangePlanAdapter.this.notifyDataSetChanged();
                                 Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                                 notifyDataSetChanged();
                             }
                         })
                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 dialogInterface.cancel();
                             }
                         })
                         .create().show();
                return false;

            }
        });



    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            value = (TextView) itemView.findViewById(R.id.key);
        }


    }
}