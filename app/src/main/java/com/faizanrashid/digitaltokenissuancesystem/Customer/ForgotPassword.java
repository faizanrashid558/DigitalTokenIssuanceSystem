package com.faizanrashid.digitaltokenissuancesystem.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faizanrashid.digitaltokenissuancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText edtCForgotPass;
    Button btnGetLink;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtCForgotPass = (EditText) findViewById(R.id.edtCForgotPass);
        btnGetLink = (Button) findViewById(R.id.btnGetLink);
        progressBar = (ProgressBar) findViewById(R.id. progressBar);
        mAuth = FirebaseAuth.getInstance();

        btnGetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = edtCForgotPass.getText().toString().trim();
        if (email.isEmpty()) {
            edtCForgotPass.setError("Email is required");
            edtCForgotPass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtCForgotPass.setError("Please provide valid email");
            edtCForgotPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email to reset password", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CustomerSigninActivity.class));
                    finish();
                }else{
                    Toast.makeText(ForgotPassword.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}