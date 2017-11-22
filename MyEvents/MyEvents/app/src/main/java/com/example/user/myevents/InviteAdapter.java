package com.example.user.myevents;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteHolder> {
    private List<User> itemList ;
    public Context context;
    public ArrayList<Boolean> inviteChecked;

    public InviteAdapter(List<User> list, Context context) {

        itemList = list;
        this.context = context;
        inviteChecked=new ArrayList<>();
    }


    @Override
    public InviteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.invite_list_item, null);
        InviteHolder inviteHolder=new InviteHolder(view);
        return inviteHolder;
    }

    @Override
    public void onBindViewHolder(final InviteHolder inviteholder, final int position) {
        final User user=itemList.get(position);
        inviteholder.inviteName.setText(user.username);
        Picasso.with(context).load(user.photoURL).fit().into(inviteholder.invitePic);
        inviteholder.inviteCheckbox.setTag(position);
        inviteChecked.add(false);
        inviteholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inviteholder.inviteCheckbox.isChecked()){
                    inviteholder.inviteCheckbox.setChecked(false);
                    inviteChecked.set(position,false);
                    inviteholder.itemView.setBackgroundColor(Color.rgb(109,109,109));
                }
                else {
                    inviteholder.inviteCheckbox.setChecked(true);
                    inviteChecked.set(position,true);
                    inviteholder.itemView.setBackgroundColor(Color.rgb(42,42,42));
                }

            }
        });
        //inviteholder.inviteCheckbox.setOnCheckedChangeListener(this);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /*@Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        inviteChecked.put((Integer) compoundButton.getTag(), b);
        if(b){
            Log.d("BOOLEAN VALUE=", "true");
            Log.d("BOOLEAN POSITION",Integer.toString((int)compoundButton.getTag()));
        }
        else Log.d("BOOLEAN VALUE=","false");

    }*/

    public static class InviteHolder extends RecyclerView.ViewHolder {
        public TextView inviteName;
        public ImageView invitePic;
        public CheckBox inviteCheckbox;
        public View itemView;



        public InviteHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            inviteName = (TextView) itemView.findViewById(R.id.inviteName);
            invitePic = (ImageView) itemView.findViewById(R.id.inviteProfilePic);
            inviteCheckbox=(CheckBox) itemView.findViewById(R.id.inviteCheckbox);
        }
    }


}
