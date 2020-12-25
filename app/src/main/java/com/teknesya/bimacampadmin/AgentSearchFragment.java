package com.teknesya.bimacampadmin;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.bimacampadmin.Adapter.CustomerRecyclerAdapter;


import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static android.widget.Toast.LENGTH_LONG;


public class AgentSearchFragment extends AppCompatActivity implements CustomerRecyclerAdapter.OnLoadMoreListener {
    private RecyclerView recyclerView;
    private CustomerRecyclerAdapter adapter;
    private List<CustomerlListings> listItems;
    private List<CustomerlListings> subListItems;
    private DatabaseReference mRoot;
    private String mLastKey = "";
    //FirebaseAuth mAuth;
    //ImageView filter;
    String mAuth="";

    //
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private boolean onPause = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agentsearchcustomer);

        mRoot = FirebaseDatabase.getInstance().getReference();
        mRoot.keepSynced(true);
        //mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listItems = new ArrayList<>();
        subListItems = new ArrayList<>();
        //  initSpinner();


        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
//        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimary));


        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refressclose();
                getData();
            }
        });
        //loadingDialog.show();
        mWaveSwipeRefreshLayout.setRefreshing(true);
        refressclose();
        getData();

      }

    @Override
    protected void onPause() {
        super.onPause();
        onPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onPause) {
            listItems.clear();
            onPause = false;
            getData();
        }
    }

    private void getData() {

        listItems.clear();
        listItems = new ArrayList<>();
        //List to add item through database
        Query listingQuery = FirebaseDatabase.getInstance().getReference()
                .child("users").child("customer").child("registered")
                .child("detail")
                .orderByKey().limitToFirst(10);
        listingQuery.keepSynced(true);


        try {  listingQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getChildrenCount() > 0) {

                        try {
                            Log.d("TAG","At getData");
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            String name = dataSnapshot.child("name").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String creationDate = dataSnapshot.child("creationdate").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();
                            Log.d("TAG",name+" "+type);
                            if (type.equals("agent")) {
                                String image = "";
                                try {
                                    image = dataSnapshot.child("detail").child("image").getValue().toString();
                                } catch (Exception e) {
                                    image = "Default";
                                }

                                String nodeId = dataSnapshot.getKey();
                                CustomerlListings javamesssage = new CustomerlListings(name, image, email, creationDate, nodeId);
                                listItems.add(javamesssage);
                                mLastKey = dataSnapshot.getKey();
                                setAdapter();
                            }
                        }catch (Exception e) {
                            Log.d("TAG",e.getMessage());
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    } else
                        mWaveSwipeRefreshLayout.setRefreshing(false);
    //
    //
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getChildrenCount() > 0) {

                        try {
                            Log.d("TAG","At getData");
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            String name = dataSnapshot.child("name").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String creationDate = dataSnapshot.child("creationdate").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();
                            Log.d("TAG",name+" "+type);
                            if (type.equals("agent")) {
                                String image = "";
                                try {
                                    image = dataSnapshot.child("detail").child("image").getValue().toString();
                                } catch (Exception e) {
                                    image = "Default";
                                }

                                String nodeId = dataSnapshot.getKey();
                                CustomerlListings javamesssage = new CustomerlListings(name, image, email, creationDate, nodeId);
                                listItems.add(javamesssage);
                                mLastKey = dataSnapshot.getKey();
                               // setAdapter();
                               // adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e) {
                            Log.d("TAG",e.getMessage());
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    } else
                        mWaveSwipeRefreshLayout.setRefreshing(false);

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        mWaveSwipeRefreshLayout.setRefreshing(false);
        adapter = new CustomerRecyclerAdapter(listItems, getApplicationContext(), this,mAuth);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadMore() {
        Log.d("Tag", mLastKey);
        mWaveSwipeRefreshLayout.setEnabled(true);
        mWaveSwipeRefreshLayout.setRefreshing(true);
        getMdata();
    }

    private void getMdata() {
        //List to add item through database
        Query listingQuery = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child("customer")
                .child("registered")
                .child("detail")
                .orderByKey().startAt(mLastKey).limitToFirst(21);

        try {
            listingQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getChildrenCount() > 0) {

                        try {
                            Log.d("TAG","At getData");
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            String name = dataSnapshot.child("name").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String creationDate = dataSnapshot.child("creationdate").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();
                            Log.d("TAG",name+" "+type);
                            if (type.equals("agent")) {
                                String image = "";
                                try {
                                    image = dataSnapshot.child("detail").child("image").getValue().toString();
                                } catch (Exception e) {
                                    image = "Default";
                                }

                                String nodeId = dataSnapshot.getKey();
                                CustomerlListings javamesssage = new CustomerlListings(name, image, email, creationDate, nodeId);
                                listItems.add(javamesssage);
                                mLastKey = dataSnapshot.getKey();
                                //setAdapter();
                                //adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e) {
                            Log.d("TAG",e.getMessage());
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    } else
                        mWaveSwipeRefreshLayout.setRefreshing(false);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    mWaveSwipeRefreshLayout.setEnabled(false);
                    if (dataSnapshot.getChildrenCount() > 0) {

                        try {
                            Log.d("TAG","At getData");
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                            String name = dataSnapshot.child("name").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            String creationDate = dataSnapshot.child("creationdate").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();
                            Log.d("TAG",name+" "+type);
                            if (type.equals("agent")) {
                                String image = "";
                                try {
                                    image = dataSnapshot.child("detail").child("image").getValue().toString();
                                } catch (Exception e) {
                                    image = "Default";
                                }

                                String nodeId = dataSnapshot.getKey();
                                CustomerlListings javamesssage = new CustomerlListings(name, image, email, creationDate, nodeId);
                                listItems.add(javamesssage);
                                mLastKey = dataSnapshot.getKey();
                                //setAdapter();
                               // adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e) {
                            Log.d("TAG",e.getMessage());
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {


        }
    }

    private void refressclose() {
        DatabaseReference referess = FirebaseDatabase.getInstance().getReference()
                .child("users").child("customer").child("registered")
                .child("detail");
        referess.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    //mWaveSwipeRefreshLayout.setRefreshing(true);
                } else {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "No Customer Found", LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}



