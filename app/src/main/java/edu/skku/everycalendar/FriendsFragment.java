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

import static edu.skku.everycalendar.ToastMaker.toast;

public class FriendsFragment extends Fragment {
    MainActivity activity;
    Context context;

    ListView friendsList;
    EditText searchText;

    ImageButton deleteBtn;
    ImageButton addBtn;

    CheckBox checkBox;

    private enum Mode{ DELETE, NORMAL }
    private Mode curMode = Mode.NORMAL;

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

        checkBox = rootView.findViewById(R.id.checkBox);

        activity = (MainActivity) getActivity();
        context = activity.context;

        list = new ArrayList<>();
        FriendsListItem item = new FriendsListItem("a","a",1);
        list.add(item);
        FriendsListItem item1 = new FriendsListItem("b","b",1);
        list.add(item1);
        FriendsListItem item2 = new FriendsListItem("aasdfa","a",1);
        list.add(item2);
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
                switch (curMode){
                    case NORMAL:
                        curMode = Mode.DELETE;
                        deleteBtn.setImageResource(R.drawable.ic_check);
                        checkBox.setVisibility(CheckBox.VISIBLE);
                        break;

                    case DELETE:
                        curMode = Mode.NORMAL;
                        //deleteItem();
                        deleteBtn.setImageResource(R.drawable.ic_delete_friend);
                        checkBox.setVisibility(CheckBox.INVISIBLE);
                        break;
                }
            }
        });
        return rootView;
    }

    private void deleteItem(){
        SparseBooleanArray checkedItems = friendsList.getCheckedItemPositions();
        int cnt = adapter.getCount();

        for(int i = cnt-1; i>=0; i--){
            if(checkedItems.get(i)){
                list.remove(i);
            }
        }

        friendsList.clearChoices();
        adapter.notifyDataSetChanged();
    }
}
