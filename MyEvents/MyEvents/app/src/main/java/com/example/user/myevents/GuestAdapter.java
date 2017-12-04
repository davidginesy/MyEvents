package com.example.user.myevents;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestHolder> {

    private List<User> guestList ;
    public Context context;

    public GuestAdapter(List<User> list, Context context) {

        guestList = list;
        this.context = context;
    }
    @Override
    public GuestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.guest_list_item, parent,false);
        GuestHolder guestHolder=new GuestHolder(view);
        return guestHolder;
    }

    @Override
    public void onBindViewHolder(GuestHolder holder, int position) {
        User user=guestList.get(position);
        holder.guestName.setText(user.username);
        Picasso.with(context).load(user.photoURL).fit().into(holder.guestPic);
        switch (user.hasAcceptedInvitation){
            case "accepted":
                holder.guestStatus.setImageResource(R.drawable.fui_done_check_mark);
                break;
            case "declined":
                holder.guestStatus.setImageResource(android.R.drawable.ic_delete);
                break;
            case "pending":
                holder.guestStatus.setImageResource(android.R.drawable.presence_away);
                break;
        }
        if(user.hasCar.equals("true")){
            holder.guestCar.setImageResource(R.drawable.car);
        }
    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }

    public static class GuestHolder extends RecyclerView.ViewHolder {
    public TextView guestName;
    public ImageView guestPic;
    public ImageView guestStatus;
    public ImageView guestCar;
    public View itemView;

    public GuestHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        guestName = (TextView) itemView.findViewById(R.id.guestName);
        guestPic = (ImageView) itemView.findViewById(R.id.guestProfilePic);
        guestStatus= (ImageView)itemView.findViewById(R.id.guestStatusIcon);
        guestCar=(ImageView) itemView.findViewById(R.id.guestCar);
    }
}
}
