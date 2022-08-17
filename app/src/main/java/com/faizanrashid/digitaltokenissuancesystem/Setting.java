package com.faizanrashid.digitaltokenissuancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faizanrashid.digitaltokenissuancesystem.Customer.CustomerSigninActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Setting extends AppCompatActivity {

    ProgressBar pb;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;

    Button btnName, btnEmail, btnPassword, btnPhone, btnDelete;
    LinearLayout llSetting, llName, llEmail, llPassword, llPhone;
    EditText edtName, edtEmail, edtPassword, edtPhone;
    Button btnUpdateName, btnUpdateEmail, btnUpdatePassword, btnUpdatePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        pb = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");

        // Initial setting cardview butons initialization
        btnName = (Button) findViewById(R.id.btnName);
        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnPassword = (Button) findViewById(R.id.btnPassword);
        btnPhone = (Button) findViewById(R.id.btnPhone);
        btnDelete = (Button) findViewById(R.id.btnDeleteAccount);


        // LinearLayout initialization
        llSetting = (LinearLayout) findViewById(R.id.llSetting);
        llName = (LinearLayout) findViewById(R.id.llName);
        llEmail = (LinearLayout) findViewById(R.id.llEmail);
        llPassword = (LinearLayout) findViewById(R.id.llPassword);
        llPhone = (LinearLayout) findViewById(R.id.llPhone);

        // Update email edittext initilization
        edtName = (EditText) findViewById(R.id.edtusername);
        edtEmail = (EditText) findViewById(R.id.edtuseremail);
        edtPassword = (EditText) findViewById(R.id.edtuserpassword);
        edtPhone = (EditText) findViewById(R.id.edtuserphone);

        // Button of update info initialization
        btnUpdateName = (Button) findViewById(R.id.btnUpdateName);
        btnUpdateEmail = (Button) findViewById(R.id.btnUpdateEmail);
        btnUpdatePassword = (Button) findViewById(R.id.btnUpdatePassword);
        btnUpdatePhone = (Button) findViewById(R.id.btnUpdatePhone);


        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSetting.setVisibility(View.GONE);
                llName.setVisibility(View.VISIBLE);

            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSetting.setVisibility(View.GONE);
                llEmail.setVisibility(View.VISIBLE);

            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSetting.setVisibility(View.GONE);
                llPassword.setVisibility(View.VISIBLE);

            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSetting.setVisibility(View.GONE);
                llPhone.setVisibility(View.VISIBLE);

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mUserRef.child(mUser.getUid()).removeValue();
                            Toast.makeText(Setting.this, "Account and data Deleted, will be logged out....", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CustomerSigninActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Setting.this, "Failed to delete account!"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                if (name.isEmpty()) {
                    edtName.setError("Full name is required");
                    edtName.requestFocus();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                mUserRef.child(mUser.getUid()).child("name").setValue(name);
                Toast.makeText(Setting.this, "User name updated successfully", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                llName.setVisibility(View.GONE);
                llSetting.setVisibility(View.VISIBLE);

            }
        });
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    edtEmail.setError("Email is required");
                    edtEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.setError("Please provide valid email");
                    edtEmail.requestFocus();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                mUserRef.child(mUser.getUid()).child("email").setValue(email);
                mUser.updateEmail(email);
                Toast.makeText(Setting.this, "User email updated successfully", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                llEmail.setVisibility(View.GONE);
                llSetting.setVisibility(View.VISIBLE);
            }
        });
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtPassword.getText().toString().trim();
                if (pass.isEmpty()) {
                    edtPassword.setError("Password is required");
                    edtPassword.requestFocus();
                    return;
                }
                if (pass.length() < 6) {
                    edtPassword.setError("Min length is 6 ");
                    edtPassword.requestFocus();
                    return;
                }
                if (!pass.contains("@")) {
                    edtPassword.setError("Must use @");
                    edtPassword.requestFocus();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                mUserRef.child(mUser.getUid()).child("password").setValue(pass);
                mUser.updatePassword(pass);
                Toast.makeText(Setting.this, "User password updated successfully", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                llPassword.setVisibility(View.GONE);
                llSetting.setVisibility(View.VISIBLE);
            }
        });
        btnUpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString().trim();
                if (phone.isEmpty()) {
                    edtPhone.setError("Phone is required");
                    edtPhone.requestFocus();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                mUserRef.child(mUser.getUid()).child("phone").setValue(phone);
                Toast.makeText(Setting.this, "User phone number updated successfully", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                llPhone.setVisibility(View.GONE);
                llSetting.setVisibility(View.VISIBLE);
            }
        });
    }
}