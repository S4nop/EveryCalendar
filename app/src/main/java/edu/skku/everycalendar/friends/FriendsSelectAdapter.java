package edu.skku.everycalendar.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.everycalendar.R;

public class FriendsSelectAdapter extends BaseAdapter {
    ArrayList<FriendsListItem> list;

    public FriendsSelectAdapter(ArrayList<FriendsListItem> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_friend_checkable,parent,false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView name_text = convertView.findViewById(R.id.friend_name);

        FriendsListItem item = list.get(position);

        name_text.setText(item.getFriend_name());

        return convertView;
    }
}
