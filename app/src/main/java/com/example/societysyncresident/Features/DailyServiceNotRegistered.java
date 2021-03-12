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
import java.util.Random;

public class DailyServiceNotRegistered extends AppCompatActivity {

    private EditText editTextNameDailyServices,editTextMobileNumberDailyServices,editTextProfessionDailyServices;
    private Button buttonContinueDailyServices;
    String name,profession,mobile_no,id,flat_no,block_no;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_service_not_registered);

        editTextNameDailyServices = findViewById(R.id.editTextNameDailyServices);
        editTextMobileNumberDailyServices = findViewById(R.id.editTextMobileNumberDailyServices);
        editTextProfessionDailyServices = findViewById(R.id.editTextProfessionDailyServices);

        buttonContinueDailyServices = findViewById(R.id.buttonDailyServicesContinue);

        progressDialog = new ProgressDialog(DailyServiceNotRegistered.this);

        buttonContinueDailyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    public void checkInput(){
        name = editTextNameDailyServices.getText().toString().trim();
        profession = editTextProfessionDailyServices.getText().toString().trim();
        mobile_no = editTextMobileNumberDailyServices.getText().toString().trim();

        if(name.isEmpty()){
            editTextNameDailyServices.setError("Name should be filled");
            editTextNameDailyServices.requestFocus();
            return;
        }
        if(profession.isEmpty()){
            editTextProfessionDailyServices.setError("Profession should be filled");
            editTextProfessionDailyServices.requestFocus();
            return;
        }
        if(mobile_no.isEmpty()){
            editTextProfessionDailyServices.setError("Profession Shpuld be filled");
            editTextProfessionDailyServices.requestFocus();
            return;
        }

        AddMaid();
    }

    public void AddMaid(){
        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(DailyServiceNotRegistered.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
                    Toast.makeText(DailyServiceNotRegistered.this, "This is id " + id, Toast.LENGTH_SHORT).show();
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

                final DatabaseReference firebaseDatabaseGetData = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("resident_registration").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseDatabaseGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(DailyServiceNotRegistered.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            flat_no = map.get("flat_number");
                            block_no = map.get("block_number");

                            Toast.makeText(DailyServiceNotRegistered.this, "flat_number" + flat_no, Toast.LENGTH_SHORT).show();

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

                final DatabaseReference firebaseDatabaseUpdateDS = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("daily_service_registration").child(getRandomNumberString());
                firebaseDatabaseUpdateDS.child("flatandblocknumber").setValue(flat_no+"    "+block_no);
                firebaseDatabaseUpdateDS.child("name").setValue(name);
                firebaseDatabaseUpdateDS.child("mobile_no").setValue(mobile_no);
                firebaseDatabaseUpdateDS.child("profession").setValue(profession);
                firebaseDatabaseUpdateDS.child("image_url").setValue(Camera.Image_url);

                Toast.makeText(DailyServiceNotRegistered.this, "Maid Registered Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DailyServiceNotRegistered.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

            }
        }, 8000);


    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void onBackPressed() {

        finish();
        Intent intent = new Intent(DailyServiceNotRegistered.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}