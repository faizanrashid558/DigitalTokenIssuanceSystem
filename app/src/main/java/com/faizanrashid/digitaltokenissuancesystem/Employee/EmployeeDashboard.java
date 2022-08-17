package com.faizanrashid.digitaltokenissuancesystem.Employee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.faizanrashid.digitaltokenissuancesystem.Customer.ActivityToken;
import com.faizanrashid.digitaltokenissuancesystem.Customer.CustomerDashboard;
import com.faizanrashid.digitaltokenissuancesystem.Customer.CustomerSigninActivity;
import com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeComplaint.EmployeeComplaint;
import com.faizanrashid.digitaltokenissuancesystem.R;
import com.faizanrashid.digitaltokenissuancesystem.Setting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeDashboard extends AppCompatActivity {
    Toolbar toolbare;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FloatingActionButton fabE;
    SimpleDateFormat sdf;
    String currentDateandTime;

    TextView txtCname, txtCemail, txtCphone, txtCTokenNo, txtCTokenType, txtCPayment, txtCTokenStatus, txtCDateandTime, txtCUid;
    TextView lblCPhone;
    String num, uid, name, email, phone, tokenno, tokentype, payment, tokenstatus, cUid;

    String profileImageUrlV, usernameV, useremailV;
    CircleImageView profileimageHeader;
    TextView usernameHeader, useremailHeader;

    Button btnLoad;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        toolbare = findViewById(R.id.toolbarE);
        setSupportActionBar(toolbare);
        getSupportActionBar().setTitle("Employee Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);

        txtCname = (TextView) findViewById(R.id.txtCname);
        txtCemail = (TextView) findViewById(R.id.txtCEmail);
        txtCphone = (TextView) findViewById(R.id.txtCPhone);
        txtCTokenNo = (TextView) findViewById(R.id.txtCTokenNo);
        txtCTokenType = (TextView) findViewById(R.id.txtCTokenType);
        txtCPayment = (TextView) findViewById(R.id.txtCPayment);
        txtCTokenStatus = (TextView) findViewById(R.id.txtCTokenStatus);
        txtCDateandTime = (TextView) findViewById(R.id.txtCDate);
        txtCUid = (TextView) findViewById(R.id.txtCUid);
        lblCPhone = (TextView) findViewById(R.id.lblCPhone);

        fabE = (FloatingActionButton) findViewById(R.id.fabE);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");

        btnLoad= (Button) findViewById(R.id.btnLoad);

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshdata();
            }
        });

        fabE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUid = txtCUid.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Feedback.class);
                intent.putExtra("tokenno", tokenno);
                intent.putExtra("userid", cUid);
                startActivity(intent);
            }
        });
        lblCPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = txtCphone.getText().toString();
                if(number.equals("Null") || number.equals("--")){
                    Toast.makeText(EmployeeDashboard.this, "No number found", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)));
                }
            }
        });






        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutE);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbare, R.string.open, R.string.close);
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_viewE);

        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);

        profileimageHeader = view.findViewById(R.id.userPhoto);
        usernameHeader = view.findViewById(R.id.username);
        useremailHeader = view.findViewById(R.id.useremail);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        if(mUser==null){
            startActivity(new Intent(getApplicationContext(), CustomerSigninActivity.class));
            finish();
        }else{
            mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        profileImageUrlV = snapshot.child("image").getValue().toString();
                        usernameV = snapshot.child("name").getValue().toString();
                        useremailV = snapshot.child("email").getValue().toString();
                        Picasso.get().load(profileImageUrlV).into(profileimageHeader);
                        usernameHeader.setText(usernameV);
                        useremailHeader.setText(useremailV);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void completefail(View view){
        switch (view.getId()){
            case R.id.complete:
                tokenstatus = "Completed";
                break;
            case R.id.failed:
                tokenstatus = "Failed";
                break;
            default:
                tokenstatus = " Invalid";
        }
        completedtoken();
    }
    private void completedtoken() {
        Random myRandom = new Random();
        name = txtCname.getText().toString();
        email = txtCemail.getText().toString();
        phone = txtCphone.getText().toString();
        tokenno = txtCTokenNo.getText().toString();
        tokentype = txtCTokenType.getText().toString();
        cUid = txtCUid.getText().toString();

        FirebaseDatabase.getInstance().getReference("History").child(cUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long c = snapshot.getChildrenCount();
                c = c+1;
                String count = String.valueOf(c);
                History historydetail = new History(name, email, phone, tokenno, tokentype, payment, tokenstatus,cUid);
                FirebaseDatabase.getInstance().getReference("History")
                        .child(cUid)
                        .child(count)
                        .setValue(historydetail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child("Tokendetail").child("activetoken");
                                    dref.setValue(num);
                                    Toast.makeText(EmployeeDashboard.this, "Hstory details added successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EmployeeDashboard.this, "Failed to upload to history", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        FirebaseDatabase.getInstance().getReference("History").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {


    }



    protected void refreshdata() {

        FirebaseDatabase.getInstance().getReference("Tokendetail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    num = snapshot.child("activetoken").getValue().toString();
                    int i = Integer.parseInt(num);
                    int k = i + 1;
                    int j = i;
                    String num2 = String.valueOf(j);
                    num = String.valueOf(k);
                    FirebaseDatabase.getInstance().getReference("Token").child(num2).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                name = snapshot.child("name").getValue().toString();
                                email = snapshot.child("email").getValue().toString();
                                phone = snapshot.child("phone").getValue().toString();
                                tokenno = snapshot.child("tokenNo").getValue().toString();
                                tokentype = snapshot.child("tokenType").getValue().toString();
                                payment = snapshot.child("payment").getValue().toString();
                                tokenstatus = snapshot.child("status").getValue().toString();
                                cUid = snapshot.child("uid").getValue().toString();

                                sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                                currentDateandTime = sdf.format(new Date());

                                txtCname.setText(name);
                                txtCemail.setText(email);
                                txtCphone.setText(phone);
                                txtCTokenNo.setText(tokenno);
                                txtCTokenType.setText(tokentype);
                                txtCPayment.setText(payment);
                                txtCTokenStatus.setText(tokenstatus);
                                txtCDateandTime.setText(currentDateandTime);
                                txtCUid.setText(cUid);

                            }else{
                                txtCname.setText("Null");
                                txtCemail.setText("Null");
                                txtCphone.setText("Null");
                                txtCTokenNo.setText("Null");
                                txtCTokenType.setText("Null");
                                txtCPayment.setText("Null");
                                txtCTokenStatus.setText("Null");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(EmployeeDashboard.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(EmployeeDashboard.this, "Unable to fetch active token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        View view = LayoutInflater.from(EmployeeDashboard.this).inflate(R.layout.layout_confirm, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).setCancelable(false).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        Button btnOk = view.findViewById(R.id.btnOk);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutE);
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {
            case R.id.home:
                drawer.closeDrawer(GravityCompat.START);
                Intent int_home = new Intent(getApplicationContext(), CustomerDashboard.class);
                startActivity(int_home);
                finish();
                return true;
            case R.id.account:
                drawer.closeDrawer(GravityCompat.START);
                Intent int_account = new Intent(getApplicationContext(), EmployeeAccount.class);
                startActivity(int_account);
                return true;
            case R.id.complaint:
                drawer.closeDrawer(GravityCompat.START);
                Intent start = new Intent(getApplicationContext(), EmployeeComplaint.class);
                startActivity(start);
                return true;

            case R.id.apply:
                drawer.closeDrawer(GravityCompat.START);
                Intent int_apply = new Intent(getApplicationContext(), ActivityToken.class);
                startActivity(int_apply);
                return true;

            case R.id.setting:
                // Handle the send action (for now display a toast).
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                // Handle the send action (for now display a toast).
                drawer.closeDrawer(GravityCompat.START);
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), CustomerSigninActivity.class);
                startActivity(i);
                finish();
            default:
                return false;
        }
    }

}
