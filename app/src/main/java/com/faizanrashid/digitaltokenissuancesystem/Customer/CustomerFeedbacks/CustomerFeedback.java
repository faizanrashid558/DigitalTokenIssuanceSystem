package com.faizanrashid.digitaltokenissuancesystem.Customer.CustomerFeedbacks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeComplaint.model;
import com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeComplaint.myadapter;
import com.faizanrashid.digitaltokenissuancesystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerFeedback extends AppCompatActivity {

    RecyclerView recview;
    myadapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);
        recview= (RecyclerView) findViewById(R.id.recviewcustomer);
        recview.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        FirebaseRecyclerOptions<com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeComplaint.model> options = new FirebaseRecyclerOptions.Builder<com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeComplaint.model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Feedback").child(mUser.getUid()), model.class)
                .build();

        adapter = new myadapter(options);
        recview.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}