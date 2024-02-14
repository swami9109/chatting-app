package com.example.mychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class settings extends AppCompatActivity {
    ImageView setProfile;
    EditText setName, setStatus;
    Button saveBtn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email, password;
    FirebaseUser user;
    int requestCode=10;
    Uri setImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        saveBtn = findViewById(R.id.saveBtn);
        setName = findViewById(R.id.name);
        setStatus = findViewById(R.id.Status);
        setProfile = findViewById(R.id.settingsProfile);
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = database.getReference().child("users").child(user.getUid());
        StorageReference storageReference = storage.getReference().child("Upload").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                password = snapshot.child("password").getValue().toString();
                String name = snapshot.child("userName").getValue().toString();
                String profile = snapshot.child("profileImage").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                setName.setText(name);
                setStatus.setText(status);
                Picasso.get().load(profile).into(setProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("imge/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = setName.getText().toString();
                String status = setStatus.getText().toString();
                if (setImageUri != null){
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    Users users = new Users(user.getUid(), name,email,password, finalImageUri, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(settings.this,
                                                        "Data is Save", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(settings.this,
                                                        MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(settings.this,
                                                        "Something went wrong....."
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            Users users = new Users(user.getUid(), name,email, password, finalImageUri, status);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(settings.this,
                                                "Data is Saved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(settings.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(settings.this,
                                                "Something went wrong....."
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode){
            if (data != null){
                setImageUri = data.getData();
                setProfile.setImageURI(setImageUri);
            }
        }
    }
}