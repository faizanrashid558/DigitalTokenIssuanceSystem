package com.faizanrashid.digitaltokenissuancesystem.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faizanrashid.digitaltokenissuancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityToken extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;

    Button processToken;
    RadioButton rdbUtil, rdbCash, rdbFee, rdbOther;
    EditText edtPay;
    ProgressBar progressBar;

    String uid, name, email, phone, tokenNo, tokenType, payment, status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference();

        // Button, RadioButton, ProgressBar initialization
        processToken = (Button) findViewById(R.id.process_token);
        rdbUtil = (RadioButton) findViewById(R.id.rdbUtil);
        rdbCash = (RadioButton) findViewById(R.id.rdbCash);
        rdbFee = (RadioButton) findViewById(R.id.rdbFee);
        rdbOther = (RadioButton) findViewById(R.id.rdbOther);
        edtPay = (EditText) findViewById(R.id.edtpay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Getting values for Token child node
        uid = mUser.getUid();

        FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name = snapshot.child("name").getValue().toString();
                    phone = snapshot.child("phone").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });
        email = mUser.getEmail();


        status = "Pending";



        processToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mUserRef.child("Token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        tokenNo = String.valueOf(count+1);

                        if(rdbUtil.isChecked()){
                            tokenType = "Utility Bills";
                        }else if(rdbCash.isChecked()){
                            tokenType = "Cash Deposit";
                        }else if(rdbFee.isChecked()){
                            tokenType = "School/University Fee";
                        }else{
                            tokenType = "Others..";
                        }
                        payment = edtPay.getText().toString();
                        Token token = new Token(uid, name, email, phone, tokenNo, tokenType, payment, status);
                        FirebaseDatabase.getInstance().getReference("Token")
                                .child(tokenNo)
                                .setValue(token)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child("Tokendetail").child("allocated");
                                            dref.setValue(tokenNo);
                                            Toast.makeText(ActivityToken.this, "Token No " + tokenNo + " generated", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            startActivity(new Intent(ActivityToken.this, CustomerDashboard.class));
                                            finish();
                                        } else {
                                            Toast.makeText(ActivityToken.this, "Token issuance failed", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}