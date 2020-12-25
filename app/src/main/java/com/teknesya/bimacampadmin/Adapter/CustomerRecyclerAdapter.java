package com.teknesya.bimacampadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teknesya.bimacampadmin.AgentCustomer;
import com.teknesya.bimacampadmin.AgentCustomerDetail;
import com.teknesya.bimacampadmin.AgentSearchFragment;
import com.teknesya.bimacampadmin.AgentDetail;
import com.teknesya.bimacampadmin.CustomerlListings;
import com.teknesya.bimacampadmin.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerRecyclerAdapter extends RecyclerView.Adapter<CustomerRecyclerAdapter.ViewHolder> {


    private List<CustomerlListings> listItems;
    private Context context;
    OnLoadMoreListener onLoadMoreListener;
    private String getType = "";
    private String currentID = "";

    public CustomerRecyclerAdapter(List<CustomerlListings> listItems, Context context, AgentSearchFragment onLoadMoreListener,String currentID) {
        this.listItems = listItems;
        this.context = context;
        this.onLoadMoreListener = onLoadMoreListener;
        getType = "agent";
        this.currentID = currentID;
    }
    public CustomerRecyclerAdapter(List<CustomerlListings> listItems, Context context, AgentCustomer onLoadMoreListener, String currentID) {
        this.listItems = listItems;
        this.context = context;
        this.onLoadMoreListener = onLoadMoreListener;
        getType = "customer";
        this.currentID = currentID;
    }



    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public List<CustomerlListings> getListItems() {
        return listItems;
    }

    public void setListItems(List<CustomerlListings> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_listing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CustomerlListings listItem = listItems.get(position);
        try {
            if (position==getItemCount()-1 ){
                onLoadMoreListener.onLoadMore();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Picasso.get().load(listItem.getImage())
                    .placeholder(R.drawable.logo).fit().centerCrop().into(holder.Image);
        } catch (Exception e) {
            e.printStackTrace();
        }

         final String id;
        id=listItems.get(position).getNodeId();
        holder.name.setText(listItem.getName());
        holder.plan.setText(listItem.getEmail());
        holder.email.setText(listItem.getPlan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getType.equals("agent")){
                    Intent it=new Intent(context, AgentDetail.class);
                    it.putExtra("nodeId",id);
                    context.startActivity(it);
                }
                else {
                    Intent it=new Intent(context, AgentCustomerDetail.class);
                    it.putExtra("nodeId",id);
                    it.putExtra("agentAuth",currentID);
                    context.startActivity(it);
                }

                //Toast.makeText(context,id,Toast.LENGTH_LONG).show();

            }
        });
        //Onclick action to the RecyclearView

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView Image;
        TextView plan, name, email;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = (CircleImageView) itemView.findViewById(R.id.image);

            plan = (TextView) itemView.findViewById(R.id.plan);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}
