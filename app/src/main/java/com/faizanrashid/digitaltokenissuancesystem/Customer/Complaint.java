package com.faizanrashid.digitaltokenissuancesystem.Customer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Complaint extends AppCompatActivity {
    Button btnComplaint;
    TextView txtCid, txtCemail;
    EditText edtComplaint;
    ProgressBar pbComplaint;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    String email, complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference();

        txtCid = (TextView) findViewById(R.id.txtCid);
        txtCemail = (TextView) findViewById(R.id.txtCemail);

        txtCid.setText(mUser.getUid());
        txtCemail.setText(mUser.getEmail());
        edtComplaint = (EditText) findViewById(R.id.edtComplaint);
        pbComplaint = (ProgressBar) findViewById(R.id.pbComplaint);
        btnComplaint = (Button) findViewById(R.id.btn_complaint);


        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbComplaint.setVisibility(View.VISIBLE);
                mUserRef.child("Complaint").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long c = snapshot.getChildrenCount();
                        String count = String.valueOf(c+1);

                        ComplaintModel details = new ComplaintModel(edtComplaint.getText().toString());
                        FirebaseDatabase.getInstance().getReference("Complaint")
                                .child(count)
                                .setValue(details)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Complaint.this, "Complaint issues successfuly", Toast.LENGTH_SHORT).show();
                                            pbComplaint.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(Complaint.this, "Failed to generate complaint", Toast.LENGTH_SHORT).show();
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