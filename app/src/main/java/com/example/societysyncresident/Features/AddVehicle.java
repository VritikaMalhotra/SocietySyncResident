package com.example.societysyncresident.Features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.societysyncresident.CommonClasses.Home;
import com.example.societysyncresident.CommonClasses.Login;
import com.example.societysyncresident.CommonClasses.Signup;
import com.example.societysyncresident.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class AddVehicle extends AppCompatActivity {

    private EditText editTextNumberPlate;
    private Button buttonAddVehicleAddVehicle;
    String id,flat_no,block_no,mobile_no,name;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        editTextNumberPlate = findViewById(R.id.editTextNumberPlate);
        buttonAddVehicleAddVehicle = findViewById(R.id.buttonAddVehicleAddVehicle);
        progressDialog = new ProgressDialog(AddVehicle.this);

        buttonAddVehicleAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberPlate();

            }
        });
    }

    public void checkNumberPlate(){
        String regexp = "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$"; //your regexp here

        if(editTextNumberPlate.getText().toString().trim().isEmpty()){
            editTextNumberPlate.setError("Number plate number is required");
            editTextNumberPlate.requestFocus();
            return;
        }
        if (!editTextNumberPlate.getText().toString().trim().matches(regexp)) {
            editTextNumberPlate.setError("Number plate should be in valid Indian number plate format");
            editTextNumberPlate.requestFocus();
            return;
        }
        addVehicle();
    }

    public  void addVehicle() {
        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(AddVehicle.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
                    Toast.makeText(AddVehicle.this, "This is id" + id, Toast.LENGTH_SHORT).show();
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
                progressDialog.setMessage("Plaese Wait");
                progressDialog.show();
                final DatabaseReference firebaseDatabaseCheckStatus = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("resident_registration").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseDatabaseCheckStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(AddVehicle.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            flat_no = map.get("flat_number");
                            block_no = map.get("block_number");
                            mobile_no = map.get("mobileno");
                            name = map.get("name");
                            Toast.makeText(AddVehicle.this, "flat_number" + flat_no, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 4000);

        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                final DatabaseReference firebaseDatabaseAddVehicle = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("vehicle_resident_registration").child(editTextNumberPlate.getText().toString().trim());
                Toast.makeText(AddVehicle.this, editTextNumberPlate.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                firebaseDatabaseAddVehicle.child("flat_no").setValue(flat_no);
                Toast.makeText(AddVehicle.this, flat_no, Toast.LENGTH_SHORT).show();
                firebaseDatabaseAddVehicle.child("block_no").setValue(block_no);
                Toast.makeText(AddVehicle.this, block_no, Toast.LENGTH_SHORT).show();
                firebaseDatabaseAddVehicle.child("mobile_no").setValue(mobile_no);
                Toast.makeText(AddVehicle.this, mobile_no, Toast.LENGTH_SHORT).show();
                firebaseDatabaseAddVehicle.child("name").setValue(name);
                Toast.makeText(AddVehicle.this, name, Toast.LENGTH_SHORT).show();
                Toast.makeText(AddVehicle.this, "Values added in db", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(AddVehicle.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                progressDialog.dismiss();
                startActivity(intent);

            }

        }, 8000);

    }
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(AddVehicle.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}