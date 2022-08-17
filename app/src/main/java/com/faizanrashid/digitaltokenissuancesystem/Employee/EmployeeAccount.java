package com.faizanrashid.digitaltokenissuancesystem.Employee;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faizanrashid.digitaltokenissuancesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAccount extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;

    String profileImageUrlV, usernameV, useremailV, userpasswordV, userphoneV;
    CircleImageView profileimage;
    TextView username, useremail, userpassword, userphone;
    TextView txtwarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_account);


        profileimage = findViewById(R.id.profile_image);
        username = (TextView) findViewById(R.id.tvName);
        useremail = (TextView) findViewById(R.id.tvEmail);
        userpassword = (TextView) findViewById(R.id.tvPass);
        userphone = (TextView) findViewById(R.id.tvPhone);
        txtwarning = (TextView) findViewById(R.id.txtwarning);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");




        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtwarning.setVisibility(View.VISIBLE);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (!(mUser == null)) {
            mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        profileImageUrlV = snapshot.child("image").getValue().toString();
                        usernameV = snapshot.child("name").getValue().toString();
                        useremailV = snapshot.child("email").getValue().toString();
                        userpasswordV = snapshot.child("password").getValue().toString();
                        userphoneV = snapshot.child("phone").getValue().toString();
                        Picasso.get().load(profileImageUrlV).into(profileimage);
                        username.setText(usernameV);
                        useremail.setText(useremailV);
                        userpassword.setText(userpasswordV);
                        userphone.setText(userphoneV);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    }
