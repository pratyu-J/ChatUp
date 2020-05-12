package com.example.chatup.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatup.Adapters.Users_recycler_adapter;
import com.example.chatup.Models.Chat;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseUser fuser;
    DatabaseReference reference;

    Users_recycler_adapter adapter;
    ArrayList<UserDetails> userChats;

    ArrayList<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = v.findViewById(R.id.prev_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fuser.getUid())){
                        userList.add(chat.getReciever());
                    }

                    if(chat.getReciever().equals(fuser.getUid())){
                        userList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    public void readChats(){
        userChats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userChats.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    UserDetails details = snapshot.getValue(UserDetails.class);

                    for(String id: userList){
                        if(id.equals(details.getId())){

                            if(userChats.size()!= 0){
                                for(UserDetails users: userChats){
                                    if(!users.getId().equals(details.getId())){
                                        userChats.add(details);
                                    }
                                }
                            }
                            else{

                                userChats.add(details);
                            }
                        }
                    }
                }

                adapter = new Users_recycler_adapter(getContext(),userChats);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
