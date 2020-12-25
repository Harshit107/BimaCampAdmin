package com.teknesya.bimacampadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeValue extends AppCompatActivity {


    EditText referral;
    Button referral_bt;
    EditText referred;
    Button referred_bt;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private DatabaseReference getReferralAmount;
    private DatabaseReference getReferredAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_value);


        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();

        referral = findViewById(R.id.referral_change);
        referral_bt = findViewById(R.id.referral_bt);
        referred = findViewById(R.id.referred_amount);
        referred_bt = findViewById(R.id.referred_amount_bt);

        getReferralAmount = mRoot.child("unique").child("referral").child("value").child("referral");
        getReferredAmount = mRoot.child("unique").child("referral").child("value").child("refregistered");


        // To whom we referred
        getReferredAmount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    referred.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Who referred
        getReferralAmount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    referral.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Who is referring
        referral_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String referralAmount = referral.getText().toString();
                if(referralAmount.isEmpty())
                    return;
                getReferralAmount.setValue(referralAmount);
                Snackbar.make(view,"Success", Snackbar.LENGTH_SHORT).show();
            }
        });

        //to whom we referred
        referred_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String referredAmount = referred.getText().toString();
                if(referredAmount.isEmpty())
                    return;
                getReferredAmount.setValue(referredAmount);
            }
        });




    }
}
