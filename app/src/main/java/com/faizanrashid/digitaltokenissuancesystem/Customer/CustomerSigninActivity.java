package com.faizanrashid.digitaltokenissuancesystem.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeSigninActivity;
import com.faizanrashid.digitaltokenissuancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerSigninActivity extends AppCompatActivity {
    Button btnCSignin;
    TextView txtESignin, txtCForgotPass, txtCSignup;
    EditText edtCEmail, edtCPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signin);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        btnCSignin = (Button) findViewById(R.id.btnCSignin);
        txtESignin = (TextView) findViewById(R.id.txtESignin);
        txtCForgotPass = (TextView) findViewById(R.id.txtCForgotPass);
        txtCSignup = (TextView) findViewById(R.id.txtCSignup);
        edtCEmail = (EditText) findViewById(R.id.edtCEmail);
        edtCPassword = (EditText) findViewById(R.id.edtCPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnCSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerSignin();
            }
        });
        txtESignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerSigninActivity.this, EmployeeSigninActivity.class));
                finish();
            }
        });
        txtCForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerSigninActivity.this, ForgotPassword.class));
            }
        });
        txtCSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerSigninActivity.this, SignupActivity.class));

            }
        });
    }

    private void customerSignin() {
        String email = edtCEmail.getText().toString().trim();
        String pass = edtCPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtCEmail.setError("Email is required");
            edtCEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtCEmail.setError("Please provide valid email");
            edtCEmail.requestFocus();
            return;
        }
        if (email.contains("bank.com")) {
            edtCEmail.setError("Cannot have an email of employee type");
            edtCEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            edtCPassword.setError("Password is required");
            edtCPassword.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtCPassword.setError("Min length is 6 ");
            edtCPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            if(user.isEmailVerified()){
                                startActivity(new Intent(CustomerSigninActivity.this, CustomerDashboard.class));
                                progressBar.setVisibility(View.GONE);
//                            }else{
//                                user.sendEmailVerification();
//                                Toast.makeText(CustomerSigninActivity.this, "Check your email to verify first", Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
                            }
                        else{
                            Toast.makeText(CustomerSigninActivity.this, "Failed to login customer! Check your credentials", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                }});
    }
}