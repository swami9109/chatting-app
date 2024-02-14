package com.example.mychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatWin extends AppCompatActivity {
    String reciverImg, reciverUid, reciverName, senderUID;
    CircularImageView profile;
    TextView name;
    CardView sendBtn;
    EditText textMsg;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference,chatReference;
    FirebaseDatabase database;
    public static String senderImg;
    public static String receiverImage;
    String senderRoom, reciverRoom;
    RecyclerView mmessagesAdapter;
    ArrayList<msgModelClass> msgArrayList;
    FirebaseUser user;
    messagesAdapter messagesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win2);
        database = FirebaseDatabase.getInstance();

        user = firebaseAuth.getInstance().getCurrentUser();

        mmessagesAdapter = findViewById(R.id.msgAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagesAdapter.setLayoutManager(linearLayoutManager);
        messagesAdapter = new messagesAdapter(chatWin.this, msgArrayList);
        mmessagesAdapter.setAdapter(messagesAdapter);

        reciverName = getIntent().getStringExtra("namee");
        reciverImg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        msgArrayList = new ArrayList<>();

        profile = findViewById(R.id.reciverImg);
        name= findViewById(R.id.reciverName);
        sendBtn = findViewById(R.id.sendBtn);
        textMsg = findViewById(R.id.writeMsg);

        Picasso.get().load(reciverImg).into(profile);
        name.setText(""+ reciverName);

        reference = database.getReference().child("users").child(user.getUid());
        chatReference = database.getReference().child("chats").child("senderRoom").child("messages");

        chatReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    msgModelClass messages = dataSnapshot.getValue(msgModelClass.class);
                    msgArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profileImage").getValue().toString();
                receiverImage = reciverImg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        senderUID = user.getUid();

        senderRoom = senderUID+reciverUid;
        reciverRoom = reciverUid+senderUID;

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textMsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(chatWin.this,
                            "Enter The Message First"
                            , Toast.LENGTH_SHORT).show();
                }
                textMsg.setText("");
                Date date = new Date();
                msgModelClass messagess = new msgModelClass(message, senderUID, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child("senderRoom")
                        .child("messages").child("message").push().setValue(messagess)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child("reciverRoom")
                                        .child("messages").child("message").push().setValue(messagess)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });
    }
}