package edu.skku.everycalendar.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import edu.skku.everycalendar.everytime.FriendTimetableReq;

import java.util.ArrayList;
import java.util.Map;

import edu.skku.everycalendar.R;
import edu.skku.everycalendar.dataType.TimetableData;
import edu.skku.everycalendar.functions.Utilities;
import edu.skku.everycalendar.everytime.AddFriendRequest;
import edu.skku.everycalendar.friends.FriendsListAdapter;
import edu.skku.everycalendar.friends.FriendsListItem;

public class FriendsFragment extends Fragment {
    MainActivity activity;
    Context context;

    ListView friendsList;
    EditText searchText;

    ImageButton deleteBtn;
    ImageButton addBtn;

    ArrayList<FriendsListItem> list;
    FriendsListAdapter adapter;

    Map<String, String> list_map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_friends, container, false);

        friendsList = rootView.findViewById(R.id.friendsList);
        searchText = rootView.findViewById(R.id.searchText);

        addBtn = rootView.findViewById(R.id.addBtn);
        deleteBtn = rootView.findViewById(R.id.deleteBtn);

        activity = (MainActivity) getActivity();
        context = activity.mainContext;

        list = activity.friends_list;
        adapter = new FriendsListAdapter(context, list);
        list_map = activity.friendList;

        friendsList.setTextFilterEnabled(true);
        friendsList.setAdapter(adapter);

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<TimetableData> friendTT;
                FriendTimetableReq friendTimetableReq = new FriendTimetableReq(activity.getCookie(), list_map.get(list.get(position).getFriend_name()));
                try{
                    friendTimetableReq.makeTimeTable();
                    friendTT = friendTimetableReq.getClassList();
                    //TODO : Make friend's timetable!!
                    Log.d("LOG_FRIENDTT", friendTT.toString());

                    Intent intent = new Intent(context, FriendsActivity.class);
                    startActivity(intent);
                } catch(Exception e){
                    Utilities.makeToast(context, "Cannot read friend's timetable data");
                }

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayoutView = inflater.inflate(R.layout.dialog_add_friend, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(activity.mainContext); // mainContext 변경 (-Activity.this -> this)
                builder.setTitle("친구 추가하기");
                builder.setView(alertLayoutView);
                builder.setCancelable(false); // 바깥 클릭해도 안꺼지게

                final EditText id_edit = alertLayoutView.findViewById(R.id.id_edit);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddFriendRequest addf = new AddFriendRequest(activity.getCookie());
                        String id_text = id_edit.getText().toString();
                        String rslt = addf.addFriend(id_text);
                        if(rslt != null || rslt.equals("-1")){
                            Utilities.makeToast(activity.getMainContext(), "친구 추가 요청을 실패했습니다");
                        }
                        else{
                            Utilities.makeToast(activity.getMainContext(), "친구의 에브리타임 계정으로 친구 추가 요청을 보냈습니다");
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
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
