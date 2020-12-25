package com.teknesya.bimacampadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn, btnTwitter;
    private AutoCompleteTextView email, password;
    private TextView forgotPass;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth,mauth;
    private FirebaseUser user;
    private ProgressDialog pbar;


    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initializeGUI();

        user = firebaseAuth.getCurrentUser();
        root= FirebaseDatabase.getInstance().getReference();

        if(user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this,Homepage.class));
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString();
                String inPassword = password.getText().toString();

                if(validateInput(inEmail, inPassword))
                {
                    signUser(inEmail, inPassword);
                }

            }
        });




        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,PWresetActivity.class));
            }
        });







    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        {
                            checkUser();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkUser() {

        root.child("users").child("customer").child("registered").child("detail").
        addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(firebaseAuth.getUid()))
                    sendUser();
                else {
                    pbar.dismiss();
                    mauth.signOut();
                    Toast.makeText(LoginActivity.this, "User Does not Exist With This Credential ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pbar.dismiss();
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }




    public void sendUser()
    {
        String devide_token = FirebaseInstanceId.getInstance().getToken();
        root.child("users").child("admin").child("registered").
                child("detail").child("token").setValue(devide_token);

        pbar.dismiss();
        Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(getApplicationContext(),Homepage.class));

    }

    public void signUser(String email, String password){

        pbar.setMessage("Validating...");
        pbar.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String devide_token = FirebaseInstanceId.getInstance().getToken();
                    root.child("users").child("admin").child("registered").
                            child("detail").child(firebaseAuth.getUid()).child("token").setValue(devide_token);

                    pbar.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,Homepage.class));
                }
                else{
                    pbar.dismiss();
                    Toast.makeText(LoginActivity.this,"Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initializeGUI(){

        logo = findViewById(R.id.ivLogLogo);
        ivSignIn = findViewById(R.id.ivSignIn);
        email = findViewById(R.id.atvEmailLog);
        password = findViewById(R.id.atvPasswordLog);
        forgotPass = findViewById(R.id.tvForgotPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        pbar = new ProgressDialog(this);
        pbar.setMessage("Please Wait..");

        mauth= FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }





    public boolean validateInput(String inemail, String inpassword){

        if(inemail.isEmpty()){
            email.setError("Email field is empty.");
            return false;
        }
        if(inpassword.isEmpty() || inpassword.length()<6){
            password.setError("Password is too short.");
            return false;
        }

        return true;
    }




}
