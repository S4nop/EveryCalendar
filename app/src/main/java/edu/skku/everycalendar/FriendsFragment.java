package edu.skku.everycalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    MainActivity activity;
    Context context;

    ListView friendsList;
    EditText searchText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_friends, container, false);

        friendsList = rootView.findViewById(R.id.friendsList);
        searchText = rootView.findViewById(R.id.searchText);

        activity = (MainActivity) getActivity();
        context = activity.context;

        final ArrayList<FriendsListItem> list = new ArrayList<>();
        final FriendsListAdapter adapter = new FriendsListAdapter(activity, list);

        friendsList.setTextFilterEnabled(true);
        friendsList.setAdapter(adapter);

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

        return rootView;
    }
}
