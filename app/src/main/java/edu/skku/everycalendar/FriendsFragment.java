package edu.skku.everycalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static edu.skku.everycalendar.ToastMaker.toast;

public class FriendsFragment extends Fragment {
    MainActivity activity;
    Context context;

    ListView friendsList;
    EditText searchText;

    ImageButton deleteBtn;
    ImageButton addBtn;

    ArrayList<FriendsListItem> list;
    FriendsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_friends, container, false);

        friendsList = rootView.findViewById(R.id.friendsList);
        searchText = rootView.findViewById(R.id.searchText);

        addBtn = rootView.findViewById(R.id.addBtn);
        deleteBtn = rootView.findViewById(R.id.deleteBtn);


        activity = (MainActivity) getActivity();
        context = activity.context;

        list = new ArrayList<>();

        FriendsListRequest friendsListRequest = new FriendsListRequest(activity.cookie);
        friendsListRequest.makeFriendList();

        //Get FriendsList
        Map<String, String> friendList = friendsListRequest.getFriendList();
        Iterator<String> iterator = friendList.keySet().iterator();
        while(iterator.hasNext()){
            String name = iterator.next();
            String key = friendList.get(name);
            FriendsListItem item = new FriendsListItem(name);
            list.add(item);
        }

        adapter = new FriendsListAdapter(context, list);

        friendsList.setTextFilterEnabled(true);
        friendsList.setAdapter(adapter);

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = s.toString();
                ((FriendsListAdapter)friendsList.getAdapter()).getFilter().filter(search);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }
}
