package com.example.societysyncresident.Features;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class VehicleLogsResident extends AppCompatActivity {

    String id,flat_no,block_no;
    ArrayList<String> NumberPlates = new ArrayList<>();
    ListView listView;
    ProgressDialog progressDialog;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> in_time = new ArrayList<>();
    ArrayList<String> out_time = new ArrayList<>();
    ArrayList<String> vehicle_number = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_logs_resident);

        progressDialog = new ProgressDialog(VehicleLogsResident.this);
        progressDialog.setMessage("Please Wait for a moment");
        progressDialog.show();

        getdata();

        /*date.add("23/11/99");
        in_time.add("23:11:99");
        out_time.add("24:11:11");
        vehicle_number.add("GJ 01 KD 2311");*/

        /*listView = findViewById(R.id.listviewLogsResident);
        MyAdapter adapter = new MyAdapter(this,date,in_time,out_time,vehicle_number);
        listView.setAdapter(adapter);*/

       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(VehicleLogsResident.this, position, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> date;
        ArrayList<String> in_time;
        ArrayList<String> out_time;
        ArrayList<String> vehicle_number;

        MyAdapter(Context c,ArrayList<String> date,ArrayList<String> in_time,ArrayList<String> out_time,ArrayList<String> vehicle_number){
            super(c,R.layout.row_logs_resident,R.id.textViewDateVehicleLogsResident,date);
            this.context = c;
            this.date = date;
            this.in_time = in_time;
            this.out_time = out_time;
            this.vehicle_number = vehicle_number;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_logs_resident,parent,false);
            TextView textViewDate = row.findViewById(R.id.textViewDateVehicleLogsResident);
            TextView textViewVehicleNumber = row.findViewById(R.id.textViewVehicleNumberVehicleLogsResident);
            TextView textViewInTime = row.findViewById(R.id.textViewInTimeVehicleLogsResident);
            TextView textViewOutTime = row.findViewById(R.id.textViewOutTimeVehicleLogsResident);

            textViewDate.setText(date.get(position));
            textViewVehicleNumber.setText(vehicle_number.get(position));
            textViewInTime.setText(in_time.get(position));
            textViewOutTime.setText(out_time.get(position));

            return row;

        }
    }

    public void getdata(){
        String sessionKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabaseSocietyId = FirebaseDatabase.getInstance().getReference().child("resident_session").child(sessionKey);
        firebaseDatabaseSocietyId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(VehicleLogsResident.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                } else {
                    Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                    id = map.get("society_id");
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
                            Toast.makeText(VehicleLogsResident.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            Map<StringBuffer, String> map = (Map) dataSnapshot.getValue();
                            flat_no = map.get("flat_number");
                            block_no = map.get("block_number");


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

                final DatabaseReference firebaseDatabaseCheckStatus = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("vehicle_resident_registration");
                firebaseDatabaseCheckStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(VehicleLogsResident.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            collectNumbers((Map<String,Object>) dataSnapshot.getValue());

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 4000);

        final Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                final DatabaseReference firebaseDatabaseCheckStatus = FirebaseDatabase.getInstance().getReference().child("societies").child(id).child("vehicle_logs_resident");
                firebaseDatabaseCheckStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(VehicleLogsResident.this, "Please register yourself first", Toast.LENGTH_SHORT).show();

                        } else {
                            collectLogs((Map<String,Object>) dataSnapshot.getValue());

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 4000);

    }
    private void collectNumbers(Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            String NumberPlate = entry.toString().substring(0, Math.min(entry.toString().length(), 13));
            //Get user map
            Map singleUser = (Map) entry.getValue();

            //Get phone field and append to list
            //phoneNumbers.add((String) singleUser.get("mobile_no"));
            //pinNumbers.add(pin);
            String checking = (String) singleUser.get("flat_no")+" "+(String) singleUser.get("block_no");
            if(checking.equals(flat_no+" "+block_no)){
                NumberPlates.add(NumberPlate);
            }
        }
    }

    private void collectLogs(Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            String NumberPlate = entry.toString().substring(0, Math.min(entry.toString().length(), 13));
            //Get user map
            Map singleUser = (Map) entry.getValue();

            //Get phone field and append to list
            //phoneNumbers.add((String) singleUser.get("mobile_no"));
            //pinNumbers.add(pin);
            for (String NumberPlateResident : NumberPlates){
                if(NumberPlateResident.equals(NumberPlate)){
                    date.add((String) singleUser.get("date"));
                    in_time.add((String) singleUser.get("in_time"));
                    out_time.add((String) singleUser.get("out_time"));
                    vehicle_number.add((String) singleUser.get("vehicle_no"));
                }
            }

            listView = findViewById(R.id.listviewLogsResident);
            MyAdapter adapter = new MyAdapter(this,date,in_time,out_time,vehicle_number);
            listView.setAdapter(adapter);
            progressDialog.dismiss();

        }
    }
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(VehicleLogsResident.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}