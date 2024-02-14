package com.example.mychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView logOut, cameraBtn, chatsBtn, settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference().child("users");

        cameraBtn = findViewById(R.id.cameraIcon);
        chatsBtn = findViewById(R.id.chatsIcon);
        settingsBtn = findViewById(R.id.settingsIcon);

        usersArrayList = new ArrayList<>();

        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this, usersArrayList);
        mainRecyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logOut = findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this, R.style.dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                Button yes, no;
                yes = dialog.findViewById(R.id.dialogYes);
                no = dialog.findViewById(R.id.dialogNo);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settings.class);
                startActivity(intent);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            }
        });

        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }
}