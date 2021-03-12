package com.example.societysyncresident.Features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Map;

public class CheckMaid extends AppCompatActivity {

    String id,mobile_no,name;
    private EditText editTextMobileNumberCheckMaid;
    private Button buttonCheckMobileNumberAddMaid;
    ArrayList<String> phoneNumbers;
    ArrayList<String> pinNumbers;
    public static String pinNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_maid);

        phoneNumbers = new ArrayList<>();
        pinNumbers = new ArrayList<>();

        editTextMobileNumberCheckMaid = findViewById(R.id.editTextMobileNumberCheckMaid);
        buttonCheckMobileNumberAddMaid = findViewById(R.id.buttonCheckMobileNumberAddMaid);

        buttonCheckMobileNumberAddMaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumber();
            }
        });
    }

    private void collectPhoneNumbers(Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            String pin = entry.toString().substring(0, Math.min(entry.toString().length(), 6));
            //Get user map
            Map singleUser = (Map) entry.getValue();

            //Get phone field and append to list
            phoneNumbers.add((String) singleUser.get("mobile_no"));
            pinNumbers.add(pin);
        }

        if(phoneNumbers.contains(editTextMobileNumberCheckMaid.getText().toString())){
            Toast.makeText(CheckMaid.this, "Daily Service already registered", Toast.LENGTH_SHORT).show();
            pinNumber = pinNumbers.get(phoneNumbers.indexOf(editTextMobileNumberCheckMaid.getText().toString()));
            Toast.makeText(this, "Pin : "+pinNumber, Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(CheckMaid.this, DailyServiceRegistered.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            Toast.makeText(CheckMaid.this, "Daily service not registered", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(CheckMaid.this, Camera.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    public void checkNumber(){
        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(CheckMaid.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
                    Toast.makeText(CheckMaid.this, "This is id" + id, Toast.LENGTH_SHORT).show();
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

                final DatabaseReference firebaseDatabaseCheckStatus = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("daily_service_registration");
                firebaseDatabaseCheckStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(CheckMaid.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());

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

        finish();
        Intent intent = new Intent(CheckMaid.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}