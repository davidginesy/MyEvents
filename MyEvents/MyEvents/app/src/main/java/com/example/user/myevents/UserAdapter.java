package com.example.user.myevents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.user.myevents.SearchFriendsActivity.search;
import static com.example.user.myevents.SearchFriendsActivity.selectedUserID;
import static com.example.user.myevents.SearchFriendsActivity.selectedUser;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{
    private List<User> itemList ;
    public Context context;

    public UserAdapter(List<User> list, Context context) {

        itemList = list;
        this.context = context;
    }


    @Override
    public UserAdapter.UserHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_list_item, null);

        UserHolder userHolder=new UserHolder(view);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(UserHolder userholder,final int position) {
        final User user=itemList.get(position);
        userholder.userName.setText(user.username);
        Picasso.with(context).load(user.photoURL).fit().into(userholder.userPic);

        userholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText(user.username);
                selectedUserID=user.UID;
                selectedUser=user;
                //Log.d("selectedUserID=",selectedUserID);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }




    public static class UserHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView userPic;
        public View itemView;

        public UserHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            userName = (TextView) itemView.findViewById(R.id.friendName);
            userPic = (ImageView) itemView.findViewById(R.id.friendProfilePic);
        }
    }
}
