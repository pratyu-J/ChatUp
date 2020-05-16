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
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;

import java.util.ArrayList;
import java.util.Collection;


public class Users_recycler_adapter extends RecyclerView.Adapter<Users_recycler_adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserDetails> mUser;
   // private ArrayList<UserDetails> fullList;
    private boolean isChat;

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                username = itemView.findViewById(R.id.userdisp);
                img = itemView.findViewById(R.id.profile_pic);
                img_on = itemView.findViewById(R.id.img_on);
                img_off = itemView.findViewById(R.id.img_off);



        }
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
