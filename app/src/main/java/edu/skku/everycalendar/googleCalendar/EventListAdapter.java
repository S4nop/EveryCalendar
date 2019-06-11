package edu.skku.everycalendar.googleCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.everycalendar.R;

public class EventListAdapter extends BaseAdapter implements Filterable {
    ArrayList<EventListItem> listViewItemList;
    ArrayList<EventListItem> filteredItemList;
    Context context;
    Filter listFilter;

    public EventListAdapter(ArrayList<EventListItem> listViewItemList, Context context) {
        this.listViewItemList = listViewItemList;
        this.filteredItemList = listViewItemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_event, null);
        }
        TextView event_name = convertView.findViewById(R.id.event_name);
        TextView event_date = convertView.findViewById(R.id.event_date);

        event_name.setText(filteredItemList.get(position).getEvent_name());
        event_date.setText(filteredItemList.get(position).getEvent_st_date()+" ~ "+
                filteredItemList.get(position).getEvent_ed_date());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
