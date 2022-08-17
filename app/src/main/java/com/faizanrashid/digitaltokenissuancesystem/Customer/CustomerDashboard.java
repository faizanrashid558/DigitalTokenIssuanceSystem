package com.faizanrashid.digitaltokenissuancesystem.Customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.CircularPropagation;
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

import com.faizanrashid.digitaltokenissuancesystem.Customer.CustomerFeedbacks.CustomerFeedback;
import com.faizanrashid.digitaltokenissuancesystem.R;
import com.faizanrashid.digitaltokenissuancesystem.Setting;
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

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerDashboard extends AppCompatActivity{
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    CircleImageView makeacall;

    String profileImageUrlV, usernameV, useremailV;
    CircleImageView profileimageHeader;
    TextView usernameHeader, useremailHeader;

    TextView tvAllocated, tvcounter, tvActive;

    FloatingActionButton fab;
    Button applytoken;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_menu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Users");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        profileimageHeader = view.findViewById(R.id.userPhoto);
        usernameHeader = view.findViewById(R.id.username);
        useremailHeader = view.findViewById(R.id.useremail);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        applytoken = (Button) findViewById(R.id.apply_token);
        makeacall = (CircleImageView) findViewById(R.id.makeacall);


        tvActive = (TextView) findViewById(R.id.activeToken);
        tvAllocated = (TextView) findViewById(R.id.allocated);
        tvcounter = (TextView) findViewById(R.id.counter);


        FirebaseDatabase.getInstance().getReference("Tokendetail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tvAllocated.setText(snapshot.child("allocated").getValue().toString());
                    tvcounter.setText(snapshot.child("counter").getValue().toString());
                    tvActive.setText(snapshot.child("activetoken").getValue().toString());
                }else{
                    Toast.makeText(CustomerDashboard.this, "Unable to fetch Token details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        applytoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityToken.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerDashboard.this, Complaint.class));
            }
        });
        makeacall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone("0517127222");
            }
        });

    }

    private void dialContactPhone(String s) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", s, null)));
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                    Toast.makeText(CustomerDashboard.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        View view = LayoutInflater.from(CustomerDashboard.this).inflate(R.layout.layout_confirm, null);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (menuItem.getItemId()) {
            case R.id.home:
                drawer.closeDrawer(GravityCompat.START);
                Intent int_home = new Intent(getApplicationContext(), CustomerDashboard.class);
                startActivity(int_home);
                finish();
                return true;
            case R.id.account:
                drawer.closeDrawer(GravityCompat.START);
                Intent int_account = new Intent(getApplicationContext(), CustomerAccount.class);
                startActivity(int_account);
                return true;

            case R.id.apply:
                drawer.closeDrawer(GravityCompat.START);
                Intent int_apply = new Intent(getApplicationContext(),ActivityToken.class);
                startActivity(int_apply);
                return true;
            case R.id.feedback:
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(), CustomerFeedback.class));
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
