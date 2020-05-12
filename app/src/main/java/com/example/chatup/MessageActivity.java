package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatup.Adapters.MessageAdapter;
import com.example.chatup.Models.Chat;
import com.example.chatup.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    TextView userName;
    CircleImageView cicleImage;
    Toolbar toolbar;
    Intent intent;
    FirebaseUser fUser;
    DatabaseReference reference;
    ImageView send;
    EditText txt_msg;

    MessageAdapter messageAdapter;

    ArrayList<Chat> mChat;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userName = findViewById(R.id.other_guy_name);
        cicleImage = findViewById(R.id.other_guy_pic);

        send = findViewById(R.id.btn_send);
        txt_msg = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.chatrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    intent = getIntent();

    final String userid = intent.getStringExtra("userid");

    send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = txt_msg.getText().toString();
            if(!message.equals("")){
                sendMessage(fUser.getUid(), userid, message);
            }
            else {
                Toast.makeText(MessageActivity.this, "You cannot send empty message", Toast.LENGTH_SHORT).show();
            }

            txt_msg.setText("");
        }
    });


    fUser = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserDetails detail = dataSnapshot.getValue(UserDetails.class);
            userName.setText(detail.getUsername());

            if(detail.getImageUrl().equals("default")){
                cicleImage.setImageResource(R.drawable.person_vector);
            }
            else {
                Glide.with(MessageActivity.this).load(detail.getImageUrl()).into(cicleImage);
            }
                readMessage(fUser.getUid(), userid, detail.getImageUrl());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

    public void sendMessage(String sender, String reciever, String message){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hash = new HashMap<>();
        hash.put("sender", sender);
        hash.put("reciever", reciever);
        hash.put("message", message);

        ref.child("chats").push().setValue(hash);
    }

    public void readMessage(final String myId, final String userid, final String imgUrl){

        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReciever().equals(myId) && chat.getSender().equals(userid) || chat.getReciever().equals(userid) && chat.getSender().equals(myId)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imgUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
