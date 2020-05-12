package com.example.chatup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatup.MessageActivity;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;

import java.util.ArrayList;


public class Users_recycler_adapter extends RecyclerView.Adapter<Users_recycler_adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserDetails> mUser;

    public Users_recycler_adapter(){

    }

    public Users_recycler_adapter(Context mContext, ArrayList<UserDetails> mUser){
        this.mContext = mContext;
        this.mUser = mUser;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_recycler_layout, parent, false);
        return new Users_recycler_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Users_recycler_adapter.ViewHolder holder, int position) {
            final UserDetails curr = mUser.get(position);

            holder.username.setText(curr.getUsername());

            if(curr.getImageUrl().equals("default")){
                holder.img.setImageResource(R.drawable.person_vector);

            }
            else {
                Glide.with(mContext).load(curr.getImageUrl()).into(holder.img);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("userid", curr.getId());
                    mContext.startActivity(intent);

                }
            });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                username = itemView.findViewById(R.id.userdisp);
                img = itemView.findViewById(R.id.profile_pic);

        }
    }
}
