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
import com.example.societysyncresident.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AddComplaint extends AppCompatActivity {

    private EditText editTextComplaint;
    private Button buttonAddComplaintAddComplaint;
    ProgressDialog progressDialog;
    String flat_no,block_no,mobile_no,name,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        editTextComplaint = findViewById(R.id.editTextComplaint);
        buttonAddComplaintAddComplaint = findViewById(R.id.buttonAddComplaintAddComplaint);
        progressDialog = new ProgressDialog(AddComplaint.this);

        buttonAddComplaintAddComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkComplaint();
            }
        });
    }
    public void checkComplaint(){
        if(editTextComplaint.getText().toString().isEmpty()){
            editTextComplaint.setError("Complaint cannot be empty");
            editTextComplaint.requestFocus();
            return;
        }
        addComplaint();
    }

    public  void addComplaint(){
        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(AddComplaint.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
                    Toast.makeText(AddComplaint.this, "This is id" + id, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddComplaint.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            flat_no = map.get("flat_number");
                            block_no = map.get("block_number");
                            mobile_no = map.get("mobileno");
                            name = map.get("name");
                            Toast.makeText(AddComplaint.this, "flat_number" + flat_no, Toast.LENGTH_SHORT).show();

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
                String root = flat_no+"_"+block_no+"_"+getCurrentDateForId()+"_"+getCurrentTime();
                final DatabaseReference firebaseDatabaseAddComplaint = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("resident_complaints").child(root);
                Toast.makeText(AddComplaint.this, editTextComplaint.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                firebaseDatabaseAddComplaint.child("flat_no").setValue(flat_no);
                firebaseDatabaseAddComplaint.child("block_no").setValue(block_no);
                firebaseDatabaseAddComplaint.child("mobile_no").setValue(mobile_no);
                firebaseDatabaseAddComplaint.child("complaint").setValue(editTextComplaint.getText().toString().trim());
                firebaseDatabaseAddComplaint.child("name").setValue(name);
                firebaseDatabaseAddComplaint.child("Date").setValue(getCurrentDate());
                firebaseDatabaseAddComplaint.child("time").setValue(getCurrentTime());
                firebaseDatabaseAddComplaint.child("status").setValue("false");
                Toast.makeText(AddComplaint.this, "Values added in db", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(AddComplaint.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                progressDialog.dismiss();
                startActivity(intent);

            }

        }, 8000);

    }
    public String getCurrentDate()
    {
        DateFormat df = new SimpleDateFormat("dd:MM:yy");
        java.util.Date dateobj = new Date();
        String date = df.format(dateobj);
        return date;
    }
    public String getCurrentDateForId()
    {
        DateFormat df = new SimpleDateFormat("dd-MM-yy");
        java.util.Date dateobj = new Date();
        String date = df.format(dateobj);
        return date;
    }
    public String getCurrentTime()
    {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date timeobj = new Date();
        String time = df.format(timeobj);
        return time;
    }

    public void onBackPressed() {

        finish();
        Intent intent = new Intent(AddComplaint.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}