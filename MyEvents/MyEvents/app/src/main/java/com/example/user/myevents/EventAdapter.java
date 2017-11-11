package com.example.user.myevents;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private List<Event> eventList;
    private Context mContext;
    public EventAdapter(Context context,List<Event> eventList){
        super(context,R.layout.event_item,eventList);
        this.eventList=eventList;
        this.mContext=context;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Event event = getItem(position);
        final View result;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.event_item, parent, false);
        TextView textview = (TextView) convertView.findViewById(R.id.displayEventInfo);
        textview.setText(event.toString());
        return convertView;
    }
}