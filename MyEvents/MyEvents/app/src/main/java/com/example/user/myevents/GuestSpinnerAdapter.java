package com.example.user.myevents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class GuestSpinnerAdapter extends ArrayAdapter {
    private List<User> guestList ;
    public Context context;

    public GuestSpinnerAdapter(Context context, int resource,  List<User> guestList) {
        super(context, resource, guestList);
        this.guestList=guestList;
        this.context=context;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
        User user=guestList.get(position);
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_spinner_item,null);

        ImageView imageView=(ImageView) view.findViewById(R.id.spinnerProfilePic);
        Picasso.with(context).load(user.photoURL).fit().into(imageView);

        TextView textView=(TextView) view.findViewById(R.id.spinnerName);
        textView.setText(user.username);

        return view;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
