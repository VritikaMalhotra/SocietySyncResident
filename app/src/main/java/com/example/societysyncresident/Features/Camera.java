package com.example.societysyncresident.Features;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.societysyncresident.CommonClasses.Home;
import com.example.societysyncresident.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Camera extends AppCompatActivity {

    private Button buttonCamera;
    private Button buttonUpload;
    private ImageView photo;
    private Bitmap Image;

    private StorageReference mStorageRef;
    ProgressDialog progressDialog;
    public static String Image_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        buttonCamera = findViewById(R.id.buttonCamera);
        buttonUpload = findViewById(R.id.buttonUpload);
        progressDialog = new ProgressDialog(Camera.this);
        photo = findViewById(R.id.imageViewCamera);

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,0);
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==0 && resultCode == RESULT_OK){
            Image = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(Image);
        }
    }

    private void upload(){
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.JPEG,100,stream);

        final String random = UUID.randomUUID().toString();
        StorageReference imageRef = mStorageRef.child("images/"+random);

        byte[] b = stream.toByteArray();

        imageRef.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image_url = uri.toString();
                        Toast.makeText(Camera.this, Image_url, Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.dismiss();
                Toast.makeText(Camera.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(Camera.this, DailyServiceNotRegistered.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Camera.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(Camera.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(Camera.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}