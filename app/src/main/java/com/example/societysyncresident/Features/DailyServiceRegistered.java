package com.example.societysyncresident.Features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.societysyncresident.CommonClasses.Home;
import com.example.societysyncresident.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DailyServiceRegistered extends AppCompatActivity {

    String id,flatandblocknumber,flat_no,block_no;
    Button buttonDailyServiceRegisteredContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_service_registered);

        buttonDailyServiceRegisteredContinue = findViewById(R.id.buttonDailyServiceRegisteredContinue);

        buttonDailyServiceRegisteredContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(DailyServiceRegistered.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(DailyServiceRegistered.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
                    Toast.makeText(DailyServiceRegistered.this, "This is id" + id, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                final DatabaseReference firebaseDatabaseCheckStatus = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("resident_registration").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseDatabaseCheckStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(DailyServiceRegistered.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            flat_no = map.get("flat_number");
                            block_no = map.get("block_number");

                            Toast.makeText(DailyServiceRegistered.this, "flat_number" + flat_no, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 4000);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final DatabaseReference firebaseDatabaseUpdateDS = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("daily_service_registration").child(CheckMaid.pinNumber);
                firebaseDatabaseUpdateDS.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(DailyServiceRegistered.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            flatandblocknumber = map.get("flatandblocknumber");
                            Toast.makeText(DailyServiceRegistered.this, "fnb : "+flatandblocknumber, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }, 8000);

        final Handler handler3 = new Handler(Looper.getMainLooper());
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {

                final DatabaseReference firebaseDatabaseUpdateDS = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("daily_service_registration").child(CheckMaid.pinNumber);
                firebaseDatabaseUpdateDS.child("flatandblocknumber").setValue(flatandblocknumber+","+flat_no+"    "+block_no);
            }
        }, 12000);
    }
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(DailyServiceRegistered.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}