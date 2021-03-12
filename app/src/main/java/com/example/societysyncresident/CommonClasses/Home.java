package com.example.societysyncresident.CommonClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.societysyncresident.Features.AddComplaint;
import com.example.societysyncresident.Features.AddVehicle;
import com.example.societysyncresident.Features.Camera;
import com.example.societysyncresident.Features.CheckMaid;
import com.example.societysyncresident.Features.VehicleLogsResident;
import com.example.societysyncresident.Features.ViewNotice;
import com.example.societysyncresident.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Home extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toast backToast;
    private long backPressed;

    private TextView textViewName;
    private Button buttonAddVehicle, buttonAddComplaint, buttonViewNotice, buttonAddMaid, buttonVehicleLogsResident, buttonLogout;
    String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        buttonAddVehicle = findViewById(R.id.buttonAddVehicle);
        buttonAddComplaint = findViewById(R.id.buttonAddComplaint);
        buttonViewNotice = findViewById(R.id.buttonViewNotice);
        buttonAddMaid = findViewById(R.id.buttonAddMaid);
        buttonVehicleLogsResident = findViewById(R.id.buttonVehicleLogsResident);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewName = findViewById(R.id.textViewNameHome);


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Home.this, Login.class));
            }
        });
        buttonAddMaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Home.this, CheckMaid.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Home.this, AddVehicle.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonAddComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Home.this, AddComplaint.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Home.this, ViewNotice.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        buttonVehicleLogsResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Home.this, VehicleLogsResident.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        showName();
    }

    public void showName() {
        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(Home.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
                    Toast.makeText(Home.this, "This is id" + id, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final DatabaseReference firebaseDatabaseGetName = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("resident_registration").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseDatabaseGetName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(Home.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            name = map.get("name");
                            Toast.makeText(Home.this, name, Toast.LENGTH_SHORT).show();
                            textViewName.setText(name);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        }, 4000);

    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (backPressed + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressed = System.currentTimeMillis();
    }
}