package com.example.chatup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;


public class Users_recycler_adapter extends RecyclerView.Adapter<Users_recycler_adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserDetails> mUser;
   // private ArrayList<UserDetails> fullList;
    private boolean isChat;
    String lastMessage;

    public Users_recycler_adapter(){

    }

    public Users_recycler_adapter(Context mContext, ArrayList<UserDetails> mUser, boolean isChat){
        this.mContext = mContext;
        this.mUser = mUser;
       // fullList = new ArrayList<>(mUser);
        this.isChat = isChat;
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

            if(isChat){
                lastMsg(curr.getId(), holder.last_msg);
            }else {
                holder.last_msg.setVisibility(View.GONE);
            }

            if(isChat) {
                if (curr.getStatus().equals("online")) {
                    holder.img_on.setVisibility(View.VISIBLE);
                    holder.img_off.setVisibility(View.GONE);

                } else {
                    holder.img_on.setVisibility(View.GONE);
                    holder.img_off.setVisibility(View.VISIBLE);
                }
            }else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);

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
        ImageView img_on;
        ImageView img_off;
        TextView last_msg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                username = itemView.findViewById(R.id.userdisp);
                img = itemView.findViewById(R.id.profile_pic);
                img_on = itemView.findViewById(R.id.img_on);
                img_off = itemView.findViewById(R.id.img_off);
                last_msg = itemView.findViewById(R.id.last_message);


        }
    }


    private void lastMsg(final String userid, final TextView last_mssg){
        lastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                       chat.getReciever().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        lastMessage = chat.getMessage();
                    }

                }

                switch (lastMessage){
                    case "defalut":
                        last_mssg.setText("no message");
                        break;
                    default:
                        last_mssg.setText(lastMessage);
                        break;
                }

                lastMessage = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*public Filter getFilter(){
        return exFilter;
}

Filter exFilter =new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<UserDetails> filterList =new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterList.addAll(fullList);
            }
            else {
                String filterPattern =constraint.toString().toLowerCase().trim();

                for(UserDetails d: fullList){

                    if(d.getUsername().toLowerCase().contains(filterPattern)){
                        filterList.add(d);
                    }
                }
            }

            FilterResults filterResults =new FilterResults();
            filterResults.values = filterList;
            return filterResults;

    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mUser.clear();
        mUser.addAll((Collection<? extends UserDetails>) results.values);
        notifyDataSetChanged();

    }
};*/

}
