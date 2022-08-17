package com.faizanrashid.digitaltokenissuancesystem.Employee;

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

import com.faizanrashid.digitaltokenissuancesystem.Customer.CustomerSigninActivity;
import com.faizanrashid.digitaltokenissuancesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmployeeSigninActivity extends AppCompatActivity {
    Button btnESignin;
    TextView txtCSignin;
    EditText edtEEmail, edtEPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_signin);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        edtEEmail = (EditText) findViewById(R.id.edtEEmail);
        edtEPassword = (EditText) findViewById(R.id.edtEPassword);

        btnESignin = (Button) findViewById(R.id.btnESignin);
        txtCSignin = (TextView) findViewById(R.id.txtCSignin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnESignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeSignin();
            }
        });
        txtCSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeeSigninActivity.this, CustomerSigninActivity.class));
                finish();
            }
        });
    }

    private void EmployeeSignin() {
        String email = edtEEmail.getText().toString().trim();
        String pass = edtEPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtEEmail.setError("Email is required");
            edtEEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEEmail.setError("Please provide valid email");
            edtEEmail.requestFocus();
            return;
        }
        if (email.contains("gmail.com")) {
            edtEEmail.setError("Cannot have an email of customer type");
            edtEEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            edtEPassword.setError("Password is required");
            edtEPassword.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtEPassword.setError("Min length is 6 ");
            edtEPassword.requestFocus();
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
                                startActivity(new Intent(EmployeeSigninActivity.this, EmployeeDashboard.class));
                                progressBar.setVisibility(View.GONE);

//                            }else{
//                                user.sendEmailVerification();
//                                Toast.makeText(EmployeeSigninActivity.this, "Check your email to verify first", Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
//                            }
                        }else{
                            Toast.makeText(EmployeeSigninActivity.this, "Failed to login employee! Check your credentials", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}