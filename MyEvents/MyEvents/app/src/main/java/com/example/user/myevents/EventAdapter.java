package com.example.user.myevents;




import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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

            convertView.setTag(holder);
        }

        Event event = eventList.get(position);
        holder.txtName.setText("Name: "+event.name);
        holder.txtTheme.setText("Theme: "+event.theme);
        holder.txtAddress.setText("Address: "+event.address);
        holder.txtDate.setText("Date: "+event.date);
        holder.txtTime.setText(event.time);
        holder.txtDescription.setText("Description: "+event.description);

        return convertView;
    }
    private class ViewHolder{
        public TextView txtName;
        public TextView txtTheme;
        public TextView txtAddress;
        public TextView txtDate;
        public TextView txtTime;
        public TextView txtDescription;
    }
}