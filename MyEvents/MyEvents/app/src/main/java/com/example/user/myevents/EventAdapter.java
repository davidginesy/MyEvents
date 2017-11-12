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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private List<Event> eventList;
    public EventAdapter(Context context,List<Event> eventList){
        super(context,0,eventList);
        this.eventList=eventList;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

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
        holder.txtName.setText("Name: "+event.name);
        holder.txtTheme.setText("Theme: "+event.theme);
        holder.txtAddress.setText("Address: "+event.address);
        holder.txtDate.setText("Date: "+event.date);
        holder.txtTime.setText("Hour: "+event.time);
        holder.txtDescription.setText("Description: "+event.description);
        holder.txtGuest.setText("Guest: "+event.guests);

        final Button deleteBtn=(Button) convertView.findViewById(R.id.eventDeleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("BUTTON CLICK :","Delete button clicked");
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

            }
        });



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