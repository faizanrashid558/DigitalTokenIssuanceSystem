package com.faizanrashid.digitaltokenissuancesystem.Employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Feedback extends AppCompatActivity {

    Button btnSendFeedback;
    TextView txtUserid;
    EditText edtFeedback;
    ProgressBar pbFeedback;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;

    String  userid;

    String ename, eemail, ephone, eFeedbackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        txtUserid = (TextView) findViewById(R.id.txtuserid);
        edtFeedback = (EditText) findViewById(R.id.edtFeedback);

        pbFeedback = (ProgressBar) findViewById(R.id.pbFeedback);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        txtUserid.setText(userid);

        btnSendFeedback = (Button) findViewById(R.id.btn_feedback);
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(Feedback.this).inflate(R.layout.layout_sent, null);
                AlertDialog dialog = new AlertDialog.Builder(Feedback.this).setView(v).setCancelable(false).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                Button btnOk = v.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pbFeedback.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference("Feedback").child(txtUserid.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                long c = snapshot.getChildrenCount();
                                c = c+1;
                                String count = String.valueOf(c);

                                eFeedbackText = edtFeedback.getText().toString();

                                Feedbackdetails feedbackdetail = new Feedbackdetails(eFeedbackText);
                                FirebaseDatabase.getInstance().getReference("Feedback")
                                        .child(userid)
                                        .child(count)
                                        .setValue(feedbackdetail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Feedback sent", Toast.LENGTH_SHORT).show();
                                                    pbFeedback.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Failed to upload the feedback", Toast.LENGTH_SHORT).show();

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
        });
    }
}