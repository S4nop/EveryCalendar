package edu.skku.everycalendar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.everytime.AddFriendRequest;
import edu.skku.everycalendar.friends.FriendsListAdapter;
import edu.skku.everycalendar.friends.FriendsListItem;
import edu.skku.everycalendar.functions.Utilities;

public class FriendsRecommActivity extends AppCompatActivity {
    ImageButton btn_back;
    ListView listView;

    ArrayList<FriendsListItem> friends_list;
    ArrayList<String> fName;
    ArrayList<String> fid;
    FriendsListAdapter adapter;
    String cookie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_recomm);

        btn_back = findViewById(R.id.btn_back);
        listView = findViewById(R.id.listView);

        friends_list = new ArrayList<>();


        fName = getIntent().getStringArrayListExtra("fName");
        fid = getIntent().getStringArrayListExtra("fid");
        cookie = getIntent().getStringExtra("Cookie");
        for(String name : fName)
            friends_list.add(new FriendsListItem(name));

        adapter = new FriendsListAdapter(FriendsRecommActivity.this, friends_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddFriendRequest afr = new AddFriendRequest(cookie);
                Utilities.makeToast(afr.addFriend(fid.get(position)));
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
