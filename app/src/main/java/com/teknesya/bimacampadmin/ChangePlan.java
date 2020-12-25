package com.teknesya.bimacampadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknesya.bimacampadmin.Adapter.ChangePlanAdapter;

import java.util.ArrayList;

public class ChangePlan extends AppCompatActivity {

    DatabaseReference mRoot,getPlan;
    FirebaseAuth mAuth;
    RecyclerView mRecycleView;
    ChangePlanAdapter planAdapter;
    ArrayList<PlanItem> planList = new ArrayList<>();

    EditText plan_et;
    Button planSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_plan);

        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();

        plan_et = findViewById(R.id.et_plan);
        planSubmit = findViewById(R.id.plan_submit);
        mRecycleView = findViewById(R.id.all_plan_recycle);

        planAdapter = new ChangePlanAdapter(planList,getApplicationContext(),ChangePlan.this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(planAdapter);
        getPlan = mRoot.child("plan");


        planSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageKey = getPlan.push().getKey();
                String plan = plan_et.getText().toString();
                if(plan.isEmpty())
                    return;
                getPlan.child(messageKey).setValue(plan);
            }
        });


        getPlan.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    String value = dataSnapshot.getValue().toString();
                    planList.add(new PlanItem(key,value));
                    planAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String key = dataSnapshot.getKey();
//                String value = dataSnapshot.getValue().toString();
//                planList.add(new PlanItem(key,value));
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                //planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
