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
import com.example.chatup.Models.Chat;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MESSAGE_TYPE_LEFT = 0;
    private static final int MESSAGE_TYPE_RIGHT = 1;

    private Context mContext;
    private ArrayList<Chat> mChat;
    private String imgUrl;
    FirebaseUser fUser;

    public MessageAdapter(){

    }

    public MessageAdapter(Context mContext, ArrayList<Chat> mChat, String imgUrl){
        this.mContext = mContext;
        this.mChat = mChat;
        this.imgUrl = imgUrl;
    }
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == MESSAGE_TYPE_RIGHT){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right, parent, false);
                return new MessageAdapter.ViewHolder(v);
            }else{
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left, parent, false);
                return new MessageAdapter.ViewHolder(v);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final Chat curr = mChat.get(position);

        holder.show_message.setText(curr.getMessage());

        if(imgUrl.equals("default")){
            holder.img.setImageResource(R.drawable.person_vector);
        }
        else {
            Glide.with(mContext).load(imgUrl).into(holder.img);
        }

        if(position == mChat.size()-1){
            if(curr.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else {
                holder.txt_seen.setText("delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView show_message;
        TextView txt_seen;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            img = itemView.findViewById(R.id.prof_img);
            txt_seen = itemView.findViewById(R.id.seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(fUser.getUid())){
            return MESSAGE_TYPE_RIGHT;
        }
        else return MESSAGE_TYPE_LEFT;
    }
}

