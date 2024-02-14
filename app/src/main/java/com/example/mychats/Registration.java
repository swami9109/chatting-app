package com.example.mychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Registration extends AppCompatActivity {

    TextView logInTv;
    EditText usernameET,emailET,passwordET,confirmPasswordET;
    Button signInBtn;
    CircularImageView profileImage;
    FirebaseAuth auth;
    Uri imageURI;
    String imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    public String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Making new account");
        progressDialog.setCancelable(false);

        logInTv = findViewById(R.id.logInTv);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        usernameET = findViewById(R.id.usernameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        signInBtn = findViewById(R.id.signInBtn);
        profileImage = findViewById(R.id.profileImage);
        auth = FirebaseAuth.getInstance();

        logInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();
                String status = "Hey whats poppin";

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "fill all the boxes"
                            , Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    emailET.setError("Type a valid Email here");
                } else if (password.length() < 6) {
                    progressDialog.dismiss();
                    passwordET.setError("Password must be more than 6 characters");
                } else if (!password.equals(confirmPassword)) {
                    progressDialog.dismiss();
                    passwordET.setError("The password doesn't match");
                }else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("users").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri = uri.toString();
                                                        Users users = new Users(imageUri,email,password,name,id,status);

                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(Registration.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(Registration.this,
                                                                            "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status = "Hey What's poppin";
                                    imageUri = "https://firebasestorage.googleapis.com/v0/b/mychats-4ef70.appspot.com/o/userclicked.png?alt=media&token=fff0e18c-5682-4797-8470-bbb6fa2a38c6";
                                    Users users = new Users(imageUri,email,password,name,id,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.show();
                                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(Registration.this,
                                                        "Error in creating the user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else {
                                Toast.makeText(Registration.this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"),10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            if (data != null){
                imageURI = data.getData();
                profileImage.setImageURI(imageURI);
            }
        }
    }
}