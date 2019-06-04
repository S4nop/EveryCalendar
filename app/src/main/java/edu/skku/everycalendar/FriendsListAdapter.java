package edu.skku.everycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendsListAdapter extends BaseAdapter implements Filterable {
    ArrayList<FriendsListItem> listViewItemList;
    ArrayList<FriendsListItem> filteredItemList;
    Context context;
    Filter listFilter;

    public FriendsListAdapter(Context context, ArrayList<FriendsListItem> listViewItemList){
        this.context = context;
        this.listViewItemList = listViewItemList;
        this.filteredItemList = listViewItemList;
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friend,null);
        }
        TextView friend_name = convertView.findViewById(R.id.friend_name);

        friend_name.setText(filteredItemList.get(position).getFriend_name());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(listFilter == null){
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0 ){
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            }else{
                ArrayList<FriendsListItem> itemList = new ArrayList<>();
                for(FriendsListItem item : listViewItemList){
                    if(item.getFriend_name().toUpperCase().contains(constraint.toString().toUpperCase())){
                        itemList.add(item);
                    }
                }

                results.values = itemList;
                results.count = itemList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (ArrayList<FriendsListItem>) results.values;

            if(results.count>0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    }
}
