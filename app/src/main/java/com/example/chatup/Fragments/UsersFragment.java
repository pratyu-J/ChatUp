package com.example.chatup.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatup.Adapters.Users_recycler_adapter;
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


public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    Users_recycler_adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UserDetails> mlist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = v.findViewById(R.id.recyclerUser);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mlist = new ArrayList<>();
        readUsers();

        return v;

    }

    public void readUsers(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlist.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    UserDetails details = snapshot.getValue(UserDetails.class);

                    assert details != null;
                    assert user != null;

                    if(!details.getId().equals(user.getUid())){
                        mlist.add(details);

                        Log.d("DETAILS", details.getId() + " " + details.getUsername());
                    }

                }

                adapter = new Users_recycler_adapter(getContext(), mlist);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
