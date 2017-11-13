package com.example.user.myevents;




import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAdapter extends ArrayAdapter<Event> {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private List<Event> eventList;
    public EventAdapter(Context context,List<Event> eventList){
        super(context,R.layout.event_item,eventList);
        this.eventList=eventList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if(holder==null){
            holder= new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtEventName);
            holder.txtTheme = (TextView) convertView.findViewById(R.id.txtEventTheme);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtEventAddress);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtEventDate);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtEventTime);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtEventDescription);
            holder.txtGuest=(TextView) convertView.findViewById(R.id.txtEventGuest);
            convertView.setTag(holder);
        }
        Event event = eventList.get(position);
        if(event.name.length()>0) holder.txtName.setText("Name: "+event.name);
        if(event.theme.length()>0) holder.txtTheme.setText("Theme: "+event.theme);
        if(event.address.length()>0)holder.txtAddress.setText("Address: "+event.address);
        if(event.date.length()>0) holder.txtDate.setText("Date: "+event.date);
        if(event.time.length()>0) holder.txtTime.setText("Hour: "+event.time);
        if(event.description.length()>0) holder.txtDescription.setText("Description: "+event.description);
        if(event.guests.length()>0) holder.txtGuest.setText("Guest: "+event.guests);

        return convertView;
    }

    private class ViewHolder{
        public TextView txtName;
        public TextView txtTheme;
        public TextView txtAddress;
        public TextView txtDate;
        public TextView txtTime;
        public TextView txtDescription;
        public TextView txtGuest;
    }
}