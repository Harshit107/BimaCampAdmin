package com.teknesya.bimacampadmin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentDetail extends AppCompatActivity {

    ImageView call;
    TextView name;
    TextView email;
    TextView lastLogin;

    FrameLayout customerDetail;
    FrameLayout viewPresentation;
    FrameLayout leadDetail;
    DatabaseReference mRoot;
    FirebaseAuth mAuth;
    String nodeId;
    DatabaseReference setDetail;
    CircleImageView image;
    String callString="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_detail);

        call = findViewById(R.id.call);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        lastLogin = findViewById(R.id.lastlogin);
        customerDetail = findViewById(R.id.fCustomerDetail);
        viewPresentation = findViewById(R.id.fViewPresentation);
        leadDetail = findViewById(R.id.leadDetail);
        image = findViewById(R.id.image);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        Intent it = getIntent();
        nodeId = it.getStringExtra("nodeId");
        setDetail = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(nodeId);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + callString));
                startActivity(dialIntent);
            }
        });
        updateDetail();

        customerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),AgentCustomer.class);
                it.putExtra("nodeId",nodeId);
                startActivity(it);
            }
        });
        viewPresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),AgentViewPresentation.class);
                it.putExtra("nodeId",nodeId);
                startActivity(it);
            }
        });
        leadDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),ViewLead.class);
                it.putExtra("nodeId",nodeId);
                startActivity(it);
            }
        });

    }

    public  void updateDetail() {

            setDetail.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    email.setText(dataSnapshot.child("email").getValue().toString());
                    lastLogin.setText(dataSnapshot.child("lastlogin").getValue().toString());
                    try {
                        Picasso.get().load(dataSnapshot.child("image").getValue().toString())
                               .centerCrop() .placeholder(R.drawable.logo).into(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        callString = dataSnapshot.child("phone").getValue().toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                        call.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
