package edu.skku.everycalendar;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class UserLogIn {
    private static DatabaseReference mPostReference;
    final static String BASE_URL = "";

    public String id;
    public String password;
    public String name;

    private UserLogIn() { }
    //access firebase
    public static void init(){
        mPostReference = FirebaseDatabase.getInstance().getReference();
    }
    //firebase에 유저가 존재하는지 확인
    static void chkUser(String user_id, String user_pw){
        init();

        postUser(user_id,user_pw);
        setUser();
    }

    //firebase에서 얻어온 유저 정보를 class에 넣음
    public static void setUser(){

    }

    //firebase에 유저가 존재하지 않을 경우(첫 로그인일 경우) firebase에 등록
    private static void postUser(String user_id, String user_pw){
        String user_name;

        //temp init
        user_name = "temp";

        Map<String, Object> user_update = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("id",user_id);
        user.put("name",user_name);
        user.put("password",user_pw);

        user_update.put("/user/"+user_id,user);
        mPostReference.updateChildren(user_update);

        getFirebaseDatabase();
    }

    public static void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("firebase_test","changed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase_test","cancel"+databaseError);
            }
        };

        mPostReference.child("user").addValueEventListener(postListener);
    }

}
